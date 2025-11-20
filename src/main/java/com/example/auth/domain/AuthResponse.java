package com.example.auth.domain;

import java.time.Instant;

public class AuthResponse {
  private String token;
  private String username;
  private Instant expiresAt;

  public AuthResponse() {}

  public AuthResponse(String token, String username, Instant expiresAt) {
    this.token = token;
    this.username = username;
    this.expiresAt = expiresAt;
  }

  public String getToken() { return token; }
  public void setToken(String token) { this.token = token; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public Instant getExpiresAt() { return expiresAt; }
  public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}

