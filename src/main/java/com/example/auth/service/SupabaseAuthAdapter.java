package com.example.auth.service;

import com.example.auth.domain.AuthResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SupabaseAuthAdapter implements AuthService {
  private final SupabaseAuthService supabase;
  private final SupabaseAdminService admin;
  private final ObjectMapper mapper = new ObjectMapper();

  public SupabaseAuthAdapter(SupabaseAuthService supabase, SupabaseAdminService admin) {
    this.supabase = supabase;
    this.admin = admin;
  }

  @Override
  public AuthResponse register(com.example.auth.web.dto.AuthRequestRegister req) {
    SupabaseAuthService.Result res;
    if (admin != null && supabase != null && supabaseResultHasServiceKey()) {
      res = admin.createUser(req.getEmail(), req.getPassword(), req.getUsername());
    } else {
      res = supabase.signupWithMeta(req.getEmail(), req.getPassword(), req.getUsername());
    }
    if (res.getStatus() >= 200 && res.getStatus() < 300) {
      try {
        JsonNode root = mapper.readTree(res.getBody());
        String username = null;
        JsonNode user = root.get("user");
        if (user != null) {
          JsonNode meta = user.get("user_metadata");
          if (meta != null && meta.get("username") != null) username = meta.get("username").asText();
          if (username == null && user.get("email") != null) username = user.get("email").asText();
        }
        return new AuthResponse(null, username != null ? username : req.getUsername(), null);
      } catch (Exception e) {
        return new AuthResponse(null, req.getUsername(), null);
      }
    }
    throw new IllegalArgumentException("Registro en Supabase falló: " + safeErr(res.getBody()));
  }

  @Override
  public AuthResponse login(com.example.auth.web.dto.AuthRequestLogin req) {
    SupabaseAuthService.Result res = supabase.login(req.getEmail(), req.getPassword());
    if (res.getStatus() >= 200 && res.getStatus() < 300) {
      try {
        JsonNode root = mapper.readTree(res.getBody());
        String token = root.get("access_token").asText();
        // Supabase no devuelve username directamente en login; lo inferimos del email
        String username = req.getEmail();
        return new AuthResponse(token, username, null);
      } catch (Exception e) {
        throw new IllegalArgumentException("Respuesta de Supabase inválida");
      }
    }
    throw new IllegalArgumentException("Login en Supabase falló: " + safeErr(res.getBody()));
  }

  private String safeErr(String s) { return s == null ? "" : s.replace('"','\''); }

  private boolean supabaseResultHasServiceKey() {
    // Simple check: admin existe implica que hay service role configurado
    return admin != null;
  }
}

