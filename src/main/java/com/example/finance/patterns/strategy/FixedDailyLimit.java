package com.example.finance.patterns.strategy;

import java.math.BigDecimal;

public class FixedDailyLimit implements DailyLimitStrategy {
  private final BigDecimal limit;
  public FixedDailyLimit(BigDecimal limit) { this.limit = limit; }
  public BigDecimal limitFor(String userId) { return limit; }
}

