package com.example.auth.security;

import com.example.auth.SupabaseProperties;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthGuard {
  private final JwtProperties jwtProps;
  private final SupabaseProperties supabaseProps;

  public AuthGuard(JwtProperties jwtProps, SupabaseProperties supabaseProps) {
    this.jwtProps = jwtProps;
    this.supabaseProps = supabaseProps;
  }

  public String require(String authHeader) {
    if (authHeader == null || authHeader.isBlank()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falta Authorization");
    String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

    boolean supaActive = supabaseProps != null && supabaseProps.getUrl() != null && !supabaseProps.getUrl().isBlank() && supabaseProps.getAnonKey() != null && !supabaseProps.getAnonKey().isBlank();
    if (supaActive) {
      return null;
    }

    if (jwtProps != null && jwtProps.getSecret() != null && !jwtProps.getSecret().isBlank()) {
      try {
        JwtUtil util = new JwtUtil(jwtProps.getSecret(), jwtProps.getExpMinutes());
        Claims claims = util.parse(token);
        return claims.getSubject();
      } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
      }
    }
    return null;
  }
}
