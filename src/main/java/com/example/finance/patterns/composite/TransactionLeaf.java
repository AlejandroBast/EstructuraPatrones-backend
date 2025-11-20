package com.example.finance.patterns.composite;

import java.math.BigDecimal;

public class TransactionLeaf implements TransactionComponent {
  private final BigDecimal amount;
  public TransactionLeaf(BigDecimal amount) { this.amount = amount; }
  public BigDecimal total() { return amount; }
}

