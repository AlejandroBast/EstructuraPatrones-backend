package com.example.finance.patterns.decorator;

import com.example.finance.domain.Recommendation;

public abstract class RecommendationDecorator {
  protected final Recommendation base;
  public RecommendationDecorator(Recommendation base) { this.base = base; }
  public Recommendation build() { return base; }
}

