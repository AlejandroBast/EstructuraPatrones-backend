package com.example.auth.config;

import com.example.auth.SupabaseProperties;
import com.example.auth.datastruct.UserStoreSingleton;
import com.example.auth.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class SeedConfig {
  @Bean
  public CommandLineRunner seedDefaultUser(SupabaseProperties supabaseProps, BCryptPasswordEncoder encoder) {
    return args -> {
      boolean useSupabase = supabaseProps != null
        && supabaseProps.getUrl() != null && !supabaseProps.getUrl().isBlank()
        && supabaseProps.getAnonKey() != null && !supabaseProps.getAnonKey().isBlank();

      if (useSupabase) return;

      String email = System.getenv().getOrDefault("DEFAULT_USER_EMAIL", "demo@demo.com");
      String password = System.getenv().getOrDefault("DEFAULT_USER_PASSWORD", "123456");
      String username = System.getenv().getOrDefault("DEFAULT_USER_NAME", "Demo");

      var store = UserStoreSingleton.getInstance();
      if (store.findByEmail(email).isEmpty()) {
        String hash = encoder.encode(password);
        store.save(new User(username, email, hash));
      }
    };
  }
}