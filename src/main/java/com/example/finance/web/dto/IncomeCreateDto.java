package com.example.finance.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeCreateDto {
  @NotNull
  private BigDecimal amount;
  @NotNull
  private LocalDate date;
  @NotBlank
  private String description;
  @NotBlank
  private String userEmail;

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getUserEmail() { return userEmail; }
  public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}

