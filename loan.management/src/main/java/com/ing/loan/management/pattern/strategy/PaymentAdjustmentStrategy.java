package com.ing.loan.management.pattern.strategy;

import java.math.BigDecimal;

public interface PaymentAdjustmentStrategy {
    BigDecimal adjust(BigDecimal baseAmount, long daysDiff);
}
