package com.example.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public class AiProperties {
  private String apiUrl;
  private String apiKey;
  private String model;
  private String referer;
  private String title;

  public String getApiUrl() { return apiUrl; }
  public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
  public String getApiKey() { return apiKey; }
  public void setApiKey(String apiKey) { this.apiKey = apiKey; }
  public String getModel() { return model; }
  public void setModel(String model) { this.model = model; }
  public String getReferer() { return referer; }
  public void setReferer(String referer) { this.referer = referer; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
}
