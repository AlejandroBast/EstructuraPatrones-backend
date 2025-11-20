package com.example.finance.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionView {
  private String id;
  private BigDecimal amount;
  private LocalDate date;
  private String description;
  private String userId;
  private String kind;
  private String type;
  private Boolean recurring;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public String getKind() { return kind; }
  public void setKind(String kind) { this.kind = kind; }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public Boolean getRecurring() { return recurring; }
  public void setRecurring(Boolean recurring) { this.recurring = recurring; }
}

