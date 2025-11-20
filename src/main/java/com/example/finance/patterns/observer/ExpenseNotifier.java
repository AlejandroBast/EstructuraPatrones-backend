package com.example.finance.patterns.observer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpenseNotifier {
  private final List<ExpenseObserver> observers = new ArrayList<>();

  public void subscribe(ExpenseObserver o) { observers.add(o); }
  public void notifyAdded(String userId, BigDecimal amount) { for (var o : observers) o.onExpenseAdded(userId, amount); }
}

