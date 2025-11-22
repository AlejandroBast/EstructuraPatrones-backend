package com.example.finance.service;

import com.example.finance.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AiAdvisor {
  private final AiProperties props;
  private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
  private static final Logger log = LoggerFactory.getLogger(AiAdvisor.class);

  public AiAdvisor(AiProperties props) { this.props = props; }

  public List<String> advise(String prompt) {
    try {
      if (props.getApiUrl() == null || props.getApiUrl().isBlank()) {
        List<String> out = new ArrayList<>();
        out.add("Activa IA configurando AI_API_URL y AI_API_KEY");
        return out;
      }
      String model = (props.getModel() == null || props.getModel().isBlank()) ? "deepseek/deepseek-r1:free" : props.getModel();
      String sys = "Eres un asesor financiero. Devuelve EXACTAMENTE una lista de 5 recomendaciones en español, en texto plano, una por línea, sin numerar, sin prefacio ni explicación adicional.";
      String body = "{\"model\":\"" + escape(model) + "\",\"messages\":[{\"role\":\"system\",\"content\":\"" + escape(sys) + "\"},{\"role\":\"user\",\"content\":\"" + escape(prompt) + "\"}],\"temperature\":0.2}";
      HttpRequest.Builder builder = HttpRequest.newBuilder()
        .uri(URI.create(props.getApiUrl()))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json");
      if (props.getReferer() != null && !props.getReferer().isBlank()) builder.header("Referer", props.getReferer());
      if (props.getTitle() != null && !props.getTitle().isBlank()) builder.header("X-Title", props.getTitle());
      if (props.getApiKey() != null && !props.getApiKey().isBlank()) builder.header("Authorization", "Bearer " + props.getApiKey());
      HttpRequest req = builder.timeout(Duration.ofSeconds(30)).POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
      log.info("IA request url={} model={}", props.getApiUrl(), model);
      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
      log.info("IA response status={}", res.statusCode());
      if (res.statusCode() >= 200 && res.statusCode() < 300) {
        List<String> out = new ArrayList<>();
        String content = parseContent(res.body());
        if (content != null && !content.isBlank()) {
          for (String line : content.split("\n")) {
            String t = line.trim();
            if (t.isEmpty()) continue;
            if (t.startsWith("-") || t.startsWith("•") || t.matches("^\\d+\\.\\s.*")) {
              t = t.replaceFirst("^[-•]\\s?", "").replaceFirst("^\\d+\\.\\s", "");
            }
            out.add(t);
          }
        }
        return out;
      }
      List<String> out = new ArrayList<>();
      log.warn("IA non-2xx status={}", res.statusCode());
      out.add("IA sin respuesta; verifica API_URL, API_KEY y modelo");
      return out;
    } catch (Exception e) {
      List<String> out = new ArrayList<>();
      log.error("IA error", e);
      out.add("Error de IA; revisa configuración y conectividad");
      return out;
    }
  }

  private String escape(String s) { return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\""); }
  private String parseContent(String body) {
    try {
      var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
      var node = mapper.readTree(body);
      var choices = node.get("choices");
      if (choices != null && choices.isArray() && choices.size() > 0) {
        var msg = choices.get(0).get("message");
        if (msg != null && msg.get("content") != null) return msg.get("content").asText();
      }
      return null;
    } catch (Exception e) { return null; }
  }
}
