package com.example.auth.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
  private final String id;
  private String username;
  private String email;
  private String passwordHash;
  private List<String> roles = new ArrayList<>();

  public User(String username, String email, String passwordHash) {
    this.id = UUID.randomUUID().toString();
    this.username = username;
    this.email = email;
    this.passwordHash = passwordHash;
    this.roles.add("USER");
  }

  public String getId() { return id; }
  public String getUsername() { return username; }
  public String getEmail() { return email; }
  public String getPasswordHash() { return passwordHash; }
  public List<String> getRoles() { return roles; }

  public void setUsername(String username) { this.username = username; }
  public void setEmail(String email) { this.email = email; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}

