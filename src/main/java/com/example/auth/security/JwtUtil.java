package com.example.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtUtil {
  private final Key key;
  private final int expMinutes;

  public JwtUtil(String secret, int expMinutes) {
    byte[] raw = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    byte[] keyBytes = raw;
    if (raw.length < 32) {
      try {
        keyBytes = java.security.MessageDigest.getInstance("SHA-256").digest(raw);
      } catch (java.security.NoSuchAlgorithmException e) {
        keyBytes = java.util.Arrays.copyOf(raw, 32);
      }
    }
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.expMinutes = expMinutes;
  }

  public String generate(String subject, List<String> roles) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expMinutes * 60L);
    return Jwts.builder()
      .setSubject(subject)
      .claim("roles", roles)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public Claims parse(String jwt) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
  }

  public Instant expiresAt() {
    return Instant.now().plusSeconds(expMinutes * 60L);
  }
}
