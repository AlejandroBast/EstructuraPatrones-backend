package com.example.finance.domain;

public class Expense extends Transaction {
  private String type;
  private boolean recurring;

  public String getType() { return type; }
  public boolean isRecurring() { return recurring; }
  public void setType(String type) { this.type = type; }
  public void setRecurring(boolean recurring) { this.recurring = recurring; }
}

