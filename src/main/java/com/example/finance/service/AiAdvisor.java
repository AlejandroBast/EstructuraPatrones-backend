package com.example.finance.service;

import com.example.finance.config.AiProperties;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AiAdvisor {
  private final AiProperties props;
  private final HttpClient http = HttpClient.newHttpClient();

  public AiAdvisor(AiProperties props) { this.props = props; }

  public List<String> advise(String prompt) {
    try {
      if (props.getApiUrl() == null || props.getApiUrl().isBlank()) return new ArrayList<>();
      String model = (props.getModel() == null || props.getModel().isBlank()) ? "deepseek/deepseek-r1:free" : props.getModel();
      String sys = "Eres un asesor financiero. Devuelve EXACTAMENTE una lista de 5 recomendaciones en español, en texto plano, una por línea, sin numerar, sin prefacio ni explicación adicional.";
      String body = "{\"model\":\"" + escape(model) + "\",\"messages\":[{\"role\":\"system\",\"content\":\"" + escape(sys) + "\"},{\"role\":\"user\",\"content\":\"" + escape(prompt) + "\"}],\"temperature\":0.2}";
      HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(props.getApiUrl()))
        .header("Content-Type", "application/json")
        .header("Authorization", props.getApiKey() == null ? "" : ("Bearer " + props.getApiKey()))
        .header("HTTP-Referer", props.getReferer() == null ? "" : props.getReferer())
        .header("X-Title", props.getTitle() == null ? "" : props.getTitle())
        .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
        .build();
      HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
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
      return new ArrayList<>();
    } catch (Exception e) {
      return new ArrayList<>();
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
