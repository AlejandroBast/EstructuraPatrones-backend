package com.example.finance.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {
  protected String id;
  protected BigDecimal amount;
  protected LocalDate date;
  protected String description;
  protected String userId;

  public String getId() { return id; }
  public BigDecimal getAmount() { return amount; }
  public LocalDate getDate() { return date; }
  public String getDescription() { return description; }
  public String getUserId() { return userId; }

  public void setId(String id) { this.id = id; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public void setDate(LocalDate date) { this.date = date; }
  public void setDescription(String description) { this.description = description; }
  public void setUserId(String userId) { this.userId = userId; }
}

