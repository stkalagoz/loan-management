package com.ing.loan.management.generator;

import com.ing.loan.management.filter.Filter;
import com.ing.loan.management.entity.Loan;

import java.math.BigDecimal;

public class LoanGenerator {

    public static Loan getLoan() {
        Loan mockLoan = new Loan();
        mockLoan.setId(1L);
        mockLoan.setLoanAmount(new BigDecimal("5000"));
        mockLoan.setIsPaid(true);
        return mockLoan;
    }

    public static Filter getFilter() {
        Filter filter = new Filter();
        filter.setKey("isPaid");
        filter.setOperation("eq");
        filter.setValue(true);
        return filter;
    }
}
