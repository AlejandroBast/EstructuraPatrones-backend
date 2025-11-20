package com.example.auth.factory;

import com.example.auth.datastruct.UserStoreSingleton;
import com.example.auth.security.JwtProperties;
import com.example.auth.security.JwtUtil;
import com.example.auth.service.AuthService;
import com.example.auth.service.JwtAuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class JwtAuthServiceFactory extends AuthServiceFactory {
  private final UserStoreSingleton store;
  private final BCryptPasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  public JwtAuthServiceFactory(UserStoreSingleton store, BCryptPasswordEncoder encoder, JwtProperties props) {
    this.store = store;
    this.encoder = encoder;
    this.jwtUtil = new JwtUtil(props.getSecret(), props.getExpMinutes());
  }

  @Override
  public AuthService createAuthService() {
    return new JwtAuthService(store, encoder, jwtUtil);
  }
}

