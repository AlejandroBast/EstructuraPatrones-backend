package com.example.finance.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseCreateDto {
  @NotNull
  private BigDecimal amount;
  @NotBlank
  private String type;
  @NotNull
  private LocalDate date;
  @NotBlank
  private String description;
  private boolean recurring;
  @NotBlank
  private String userEmail;

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public boolean isRecurring() { return recurring; }
  public void setRecurring(boolean recurring) { this.recurring = recurring; }
  public String getUserEmail() { return userEmail; }
  public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}

