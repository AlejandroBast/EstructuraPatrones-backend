package com.example.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "finance.tables")
public class FinanceTablesProperties {
  private String income = "ingresos";
  private String expenses = "gastos";
  private String micro = "microgastos";
  private String userColumn = "user_id";

  public String getIncome() { return income; }
  public void setIncome(String income) { this.income = income; }
  public String getExpenses() { return expenses; }
  public void setExpenses(String expenses) { this.expenses = expenses; }
  public String getMicro() { return micro; }
  public void setMicro(String micro) { this.micro = micro; }
  public String getUserColumn() { return userColumn; }
  public void setUserColumn(String userColumn) { this.userColumn = userColumn; }
}
