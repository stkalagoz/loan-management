package com.ing.loan.management.pattern.strategy;

public class AdjustmentStrategyFactory {

    public PaymentAdjustmentStrategy getStrategy(long daysDiff) {
        if (daysDiff > 0) return new DiscountStrategy();
        if (daysDiff < 0) return new PenaltyStrategy();
        return new NoAdjustmentStrategy();
    }
}
