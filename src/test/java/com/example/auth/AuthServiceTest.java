package com.example.auth;

import com.example.auth.factory.JwtAuthServiceFactory;
import com.example.auth.security.JwtProperties;
import com.example.auth.service.AuthService;
import com.example.auth.web.dto.AuthRequestLogin;
import com.example.auth.web.dto.AuthRequestRegister;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
  private AuthService newService() {
    JwtProperties props = new JwtProperties();
    props.setSecret("test-secret-1234567890-test-secret-123456");
    props.setExpMinutes(60);
    var factory = new JwtAuthServiceFactory(com.example.auth.datastruct.UserStoreSingleton.getInstance(), new BCryptPasswordEncoder(), props);
    return factory.createAuthService();
  }

  @Test
  void registerThenLoginReturnsToken() {
    AuthService service = newService();
    AuthRequestRegister reg = new AuthRequestRegister();
    reg.setUsername("demo");
    reg.setEmail("demo@example.com");
    reg.setPassword("password123");
    var regRes = service.register(reg);
    assertNull(regRes.getToken());

    AuthRequestLogin login = new AuthRequestLogin();
    login.setEmail("demo@example.com");
    login.setPassword("password123");
    var loginRes = service.login(login);
    assertNotNull(loginRes.getToken());
    assertEquals("demo", loginRes.getUsername());
  }
}

