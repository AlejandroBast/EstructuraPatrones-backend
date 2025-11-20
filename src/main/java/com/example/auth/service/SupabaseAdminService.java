package com.example.auth.service;

import com.example.auth.SupabaseProperties;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseAdminService {
  private final SupabaseProperties props;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  public SupabaseAdminService(SupabaseProperties props) { this.props = props; }

  public SupabaseAuthService.Result createUser(String email, String password, String username) {
    String endpoint = props.getUrl() + "/auth/v1/admin/users";
    String body = "{\"email\":\"" + escape(email) + "\",\"password\":\"" + escape(password) + "\",\"user_metadata\":{\"username\":\"" + escape(username) + "\"},\"email_confirm\":true}";
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpoint))
        .header("Content-Type", "application/json")
        .header("apikey", props.getAnonKey())
        .header("Authorization", "Bearer " + props.getServiceRoleKey())
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return new SupabaseAuthService.Result(response.statusCode(), response.body());
    } catch (Exception e) {
      String message = "{\"error\":\"" + safeMessage(e.getMessage()) + "\"}";
      return new SupabaseAuthService.Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
  }

  public SupabaseAuthService.Result listUsers(int page, int perPage) {
    String endpoint = props.getUrl() + "/auth/v1/admin/users?page=" + page + "&per_page=" + perPage;
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpoint))
        .header("Content-Type", "application/json")
        .header("apikey", props.getAnonKey())
        .header("Authorization", "Bearer " + props.getServiceRoleKey())
        .GET()
        .build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return new SupabaseAuthService.Result(response.statusCode(), response.body());
    } catch (Exception e) {
      String message = "{\"error\":\"" + safeMessage(e.getMessage()) + "\"}";
      return new SupabaseAuthService.Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
  }

  private String escape(String s) { return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\""); }
  private String safeMessage(String s) { return s == null ? "unknown" : s.replace("\"", "'"); }
}
