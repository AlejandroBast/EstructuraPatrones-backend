package com.example.auth.service;

import com.example.auth.domain.AuthResponse;
import com.example.auth.web.dto.AuthRequestLogin;
import com.example.auth.web.dto.AuthRequestRegister;

public interface AuthService {
  AuthResponse register(AuthRequestRegister req);
  AuthResponse login(AuthRequestLogin req);
}

