package com.example.finance.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MicroExpenseCreateDto {
  @NotNull
  private BigDecimal amount;
  @NotBlank
  private String description;
  @NotNull
  private LocalDate date;
  @NotBlank
  private String userEmail;

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }
  public String getUserEmail() { return userEmail; }
  public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}

