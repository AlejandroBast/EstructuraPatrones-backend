package com.example.auth.facade;

import com.example.auth.datastruct.HistoryStack;
import com.example.auth.datastruct.RequestQueue;
import com.example.auth.datastruct.UserStoreSingleton;
import com.example.auth.domain.AuthResponse;
import com.example.auth.factory.JwtAuthServiceFactory;
import com.example.auth.security.JwtProperties;
import com.example.auth.service.AuthService;
import com.example.auth.service.SupabaseAuthAdapter;
import com.example.auth.service.SupabaseAuthService;
import com.example.auth.service.SupabaseAdminService;
import com.example.auth.web.dto.AuthRequestLogin;
import com.example.auth.web.dto.AuthRequestRegister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthFacade {
  private final RequestQueue queue = new RequestQueue();
  private final HistoryStack history = new HistoryStack();

  private final AuthService service;
  private final SupabaseAdminService admin;

  public AuthFacade(JwtProperties jwtProps,
                    BCryptPasswordEncoder encoder,
                    com.example.auth.SupabaseProperties supabaseProps) {
    boolean useSupabase = supabaseProps != null
      && supabaseProps.getUrl() != null && !supabaseProps.getUrl().isBlank()
      && supabaseProps.getAnonKey() != null && !supabaseProps.getAnonKey().isBlank();

    if (useSupabase) {
      var sp = java.util.Objects.requireNonNull(supabaseProps);
      var supa = new SupabaseAuthService(sp);
      this.admin = (sp.getServiceRoleKey() != null && !sp.getServiceRoleKey().isBlank())
        ? new SupabaseAdminService(sp)
        : null;
      this.service = new SupabaseAuthAdapter(supa, this.admin);
    } else {
      var factory = new JwtAuthServiceFactory(UserStoreSingleton.getInstance(), encoder, jwtProps);
      this.service = factory.createAuthService();
      this.admin = null;
    }
  }

  public AuthResponse register(AuthRequestRegister req) {
    queue.push("register:" + req.getEmail());
    var res = service.register(req);
    history.push("registered:" + req.getEmail());
    return res;
  }

  public AuthResponse login(AuthRequestLogin req) {
    queue.push("login:" + req.getEmail());
    var res = service.login(req);
    history.push("logged:" + req.getEmail());
    return res;
  }

  public SupabaseAuthService.Result listUsers(int page, int perPage) {
    if (admin != null) {
      return admin.listUsers(page, perPage);
    } else {
      try {
        var users = UserStoreSingleton.getInstance().list();
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(users);
        return new SupabaseAuthService.Result(200, json);
      } catch (Exception e) {
        return new SupabaseAuthService.Result(500, "{\"error\":\"internal\"}");
      }
    }
  }
}
