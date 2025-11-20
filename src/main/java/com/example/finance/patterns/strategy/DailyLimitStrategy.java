package com.example.finance.patterns.strategy;

import java.math.BigDecimal;

public interface DailyLimitStrategy {
  BigDecimal limitFor(String userId);
}

