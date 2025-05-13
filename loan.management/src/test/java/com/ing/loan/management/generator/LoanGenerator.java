package com.ing.loan.management.generator;

import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.filter.Filter;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.request.LoanRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanGenerator {
    private static final Long CUSTOMER_ID = 1L;
    private static final BigDecimal AMOUNT = new BigDecimal(5000);
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.2");
    private static final int INSTALLMENTS = 6;
    private static final Long INVALID_LOAN_ID = 99L;
    private static final Long VALID_LOAN_ID = 1L;


    public static Loan getLoan() {
        Loan mockLoan = new Loan();
        mockLoan.setId(VALID_LOAN_ID);
        mockLoan.setLoanAmount(AMOUNT);
        mockLoan.setIsPaid(true);
        mockLoan.setInstallments(getLoanInstallments());
        return mockLoan;
    }

    public static Filter getFilter() {
        Filter filter = new Filter();
        filter.setKey("isPaid");
        filter.setOperation("eq");
        filter.setValue(true);
        return filter;
    }

    public static LoanRequest getLoanRequest() {
        return new LoanRequest(CUSTOMER_ID, AMOUNT, INTEREST_RATE, INSTALLMENTS);
    }

    public static BigDecimal getLoanAmount() {
        return AMOUNT;
    }

    public static int getNumberOfInstallment() {
        return INSTALLMENTS;
    }

    public static List<LoanInstallment> getLoanInstallments() {
        List<LoanInstallment> list = new ArrayList<>();
        LoanInstallment installment1 = new LoanInstallment();
        installment1.setPaymentDate(null);
        installment1.setPaidAmount(AMOUNT);
        installment1.setAmount(AMOUNT);
        installment1.setIsPaid(true);
        installment1.setDueDate(LocalDate.now().withDayOfMonth(1).plusMonths(1));

        LoanInstallment installment2 = new LoanInstallment();
        installment2.setPaymentDate(null);
        installment2.setIsPaid(false);
        installment2.setPaidAmount(BigDecimal.valueOf(0));
        installment2.setAmount(AMOUNT);
        installment2.setDueDate(LocalDate.now().withDayOfMonth(1).plusMonths(2));
        list.add(installment1);
        list.add(installment2);
        return list;
    }

    public static LoanPayRequest getLoanPayRequest() {
        return new LoanPayRequest(VALID_LOAN_ID, AMOUNT);
    }

    public static Long getInvalidLoanId () {
        return INVALID_LOAN_ID;
    }

    public static Long getValidLoanId () {
        return VALID_LOAN_ID;
    }
}
