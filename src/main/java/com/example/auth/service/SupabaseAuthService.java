package com.example.auth.service;

import com.example.auth.SupabaseProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SupabaseAuthService {
  private final SupabaseProperties props;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  public SupabaseAuthService(SupabaseProperties props) {
    this.props = props;
  }

  public Result signup(String email, String password) {
    String endpoint = props.getUrl() + "/auth/v1/signup";
    String body = "{\"email\":\"" + escape(email) + "\",\"password\":\"" + escape(password) + "\"}";
    return callSupabase(endpoint, body);
  }

  public Result signupWithMeta(String email, String password, String username) {
    String endpoint = props.getUrl() + "/auth/v1/signup";
    String body = "{\"email\":\"" + escape(email) + "\",\"password\":\"" + escape(password) + "\",\"data\":{\"username\":\"" + escape(username) + "\"}}";
    return callSupabase(endpoint, body);
  }

  public Result login(String email, String password) {
    String endpoint = props.getUrl() + "/auth/v1/token?grant_type=password";
    String body = "{\"email\":\"" + escape(email) + "\",\"password\":\"" + escape(password) + "\"}";
    return callSupabase(endpoint, body);
  }

  private Result callSupabase(String endpoint, String body) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpoint))
        .header("Content-Type", "application/json")
        .header("apikey", props.getAnonKey())
        .header("Authorization", "Bearer " + props.getAnonKey())
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      int status = response.statusCode();
      String respBody = response.body();
      return new Result(status, respBody);
    } catch (Exception e) {
      String message = "{\"error\":\"" + safeMessage(e.getMessage()) + "\"}";
      return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
  }

  private String escape(String s) {
    return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private String safeMessage(String s) {
    return s == null ? "unknown" : s.replace("\"", "'");
  }

  public static class Result {
    private final int status;
    private final String body;

    public Result(int status, String body) { this.status = status; this.body = body; }
    public int getStatus() { return status; }
    public String getBody() { return body; }
  }
}
