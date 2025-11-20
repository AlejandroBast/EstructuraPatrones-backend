package com.example.finance.domain;

public class Recommendation {
  private String id;
  private String type;
  private String description;
  private String userId;
  private String potentialSavings;

  public String getId() { return id; }
  public String getType() { return type; }
  public String getDescription() { return description; }
  public String getUserId() { return userId; }
  public String getPotentialSavings() { return potentialSavings; }
  public void setId(String id) { this.id = id; }
  public void setType(String type) { this.type = type; }
  public void setDescription(String description) { this.description = description; }
  public void setUserId(String userId) { this.userId = userId; }
  public void setPotentialSavings(String potentialSavings) { this.potentialSavings = potentialSavings; }
}

