package com.ing.loan.management.request;

import java.math.BigDecimal;

public record LoanPayRequest(Long loanId,
                             BigDecimal incomingPayment) {
}
