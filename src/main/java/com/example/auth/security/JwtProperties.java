package com.example.auth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
  private String secret;
  private int expMinutes;

  public String getSecret() { return secret; }
  public void setSecret(String secret) { this.secret = secret; }
  public int getExpMinutes() { return expMinutes; }
  public void setExpMinutes(int expMinutes) { this.expMinutes = expMinutes; }
}

