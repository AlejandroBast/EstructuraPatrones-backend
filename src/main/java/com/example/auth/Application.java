package com.example.auth;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.example.auth", "com.example.finance"})
@EnableConfigurationProperties({SupabaseProperties.class, com.example.auth.security.JwtProperties.class, com.example.finance.config.FinanceTablesProperties.class, com.example.finance.config.AiProperties.class})
public class Application {
  public static void main(String[] args) {
    try {
      Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
      setPropIfMissing(dotenv, "SUPABASE_URL");
      setPropIfMissing(dotenv, "SUPABASE_ANON_KEY");
      setPropIfMissing(dotenv, "SUPABASE_SERVICE_ROLE");
      setPropIfMissing(dotenv, "JWT_SECRET");
      setPropIfMissing(dotenv, "JWT_EXP_MINUTES");
      setPropIfMissing(dotenv, "FINANCE_TABLE_INCOME");
      setPropIfMissing(dotenv, "FINANCE_TABLE_EXPENSES");
      setPropIfMissing(dotenv, "FINANCE_TABLE_MICRO");
      setPropIfMissing(dotenv, "AI_API_URL");
      setPropIfMissing(dotenv, "AI_API_KEY");
      setPropIfMissing(dotenv, "AI_MODEL");
      setPropIfMissing(dotenv, "AI_REFERER");
      setPropIfMissing(dotenv, "AI_TITLE");
    } catch (DotenvException ignored) {}
    SpringApplication.run(Application.class, args);
  }

  private static void setPropIfMissing(Dotenv dotenv, String key) {
    String val = dotenv.get(key);
    if (val != null && System.getenv(key) == null && System.getProperty(key) == null) {
      System.setProperty(key, val);
    }
  }
}
