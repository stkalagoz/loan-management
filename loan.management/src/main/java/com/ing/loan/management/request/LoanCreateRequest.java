package com.ing.loan.management.request;

import java.math.BigDecimal;

public record LoanCreateRequest(Long customerId,
                                BigDecimal amount,
                                BigDecimal interestRate,
                                Integer numberOfInstallments) {
}
