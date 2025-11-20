package com.example.finance.patterns.observer;

import java.math.BigDecimal;

public interface ExpenseObserver {
  void onExpenseAdded(String userId, BigDecimal amount);
}

