package com.ing.loan.management.response;

import java.math.BigDecimal;

public record LoanPaymentResponse(int installmentsPaid, BigDecimal totalPaid, boolean loanCompletelyPaid) {}
