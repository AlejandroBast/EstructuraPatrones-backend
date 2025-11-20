package com.example.finance.domain;

public class MicroExpense extends Transaction {
  private Integer dailyLimit;

  public Integer getDailyLimit() { return dailyLimit; }
  public void setDailyLimit(Integer dailyLimit) { this.dailyLimit = dailyLimit; }
}

