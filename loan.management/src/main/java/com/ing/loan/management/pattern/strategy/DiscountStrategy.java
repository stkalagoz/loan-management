package com.ing.loan.management.pattern.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountStrategy implements PaymentAdjustmentStrategy {
    private static final BigDecimal REWARD_RATE = new BigDecimal("0.001");

    @Override
    public BigDecimal adjust(BigDecimal baseAmount, long daysEarly) {
        BigDecimal discount = baseAmount
                .multiply(REWARD_RATE)
                .multiply(BigDecimal.valueOf(daysEarly))
                .setScale(2, RoundingMode.HALF_UP);
        return baseAmount.subtract(discount);
    }
}
