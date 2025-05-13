package com.ing.loan.management.pattern.strategy;

import java.math.BigDecimal;

public class NoAdjustmentStrategy implements PaymentAdjustmentStrategy {

    @Override
    public BigDecimal adjust(BigDecimal baseAmount, long unused) {
        return baseAmount;
    }
}
