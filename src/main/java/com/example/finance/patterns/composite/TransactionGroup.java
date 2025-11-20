package com.example.finance.patterns.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionGroup implements TransactionComponent {
  private final List<TransactionComponent> children = new ArrayList<>();
  public TransactionGroup add(TransactionComponent c) { children.add(c); return this; }
  public BigDecimal total() { return children.stream().map(TransactionComponent::total).reduce(BigDecimal.ZERO, BigDecimal::add); }
}

