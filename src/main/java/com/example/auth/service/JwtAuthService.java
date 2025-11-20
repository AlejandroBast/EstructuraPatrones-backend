package com.example.auth.service;

import com.example.auth.datastruct.UserStoreSingleton;
import com.example.auth.domain.AuthResponse;
import com.example.auth.domain.User;
import com.example.auth.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

public class JwtAuthService implements AuthService {
  private final UserStoreSingleton store;
  private final BCryptPasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  public JwtAuthService(UserStoreSingleton store, BCryptPasswordEncoder encoder, JwtUtil jwtUtil) {
    this.store = store;
    this.encoder = encoder;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public AuthResponse register(com.example.auth.web.dto.AuthRequestRegister req) {
    if (store.findByEmail(req.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email ya registrado");
    }
    String hash = encoder.encode(req.getPassword());
    User user = new User(req.getUsername(), req.getEmail(), hash);
    store.save(user);
    return new AuthResponse(null, user.getUsername(), null);
  }

  @Override
  public AuthResponse login(com.example.auth.web.dto.AuthRequestLogin req) {
    var userOpt = store.findByEmail(req.getEmail());
    if (userOpt.isEmpty()) { throw new IllegalArgumentException("Usuario no encontrado"); }
    var user = userOpt.get();
    if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }
    String token = jwtUtil.generate(user.getEmail(), user.getRoles());
    Instant exp = jwtUtil.expiresAt();
    return new AuthResponse(token, user.getUsername(), exp);
  }
}

