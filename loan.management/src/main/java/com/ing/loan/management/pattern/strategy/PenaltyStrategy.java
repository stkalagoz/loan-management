package com.ing.loan.management.pattern.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PenaltyStrategy implements PaymentAdjustmentStrategy {

    @Override
    public BigDecimal adjust(BigDecimal baseAmount, long daysLate) {
        BigDecimal penalty = baseAmount
                .multiply(BigDecimal.valueOf(0.001))
                .multiply(BigDecimal.valueOf(daysLate))
                .setScale(2, RoundingMode.HALF_UP);
        return baseAmount.add(penalty);
    }
}
