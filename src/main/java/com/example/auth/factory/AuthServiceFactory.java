package com.example.auth.factory;

import com.example.auth.service.AuthService;

public abstract class AuthServiceFactory {
  public abstract AuthService createAuthService();
}

