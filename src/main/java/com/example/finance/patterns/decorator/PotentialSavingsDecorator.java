package com.example.finance.patterns.decorator;

import com.example.finance.domain.Recommendation;

public class PotentialSavingsDecorator extends RecommendationDecorator {
  public PotentialSavingsDecorator(Recommendation base) { super(base); }
  public Recommendation build() { return base; }
}

