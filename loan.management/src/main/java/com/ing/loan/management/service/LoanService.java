package com.ing.loan.management.service;

import com.ing.loan.management.filter.LoanFilterRequest;
import com.ing.loan.management.filter.SpecificationBuilder;
import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.repository.LoanRepository;
import com.ing.loan.management.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerService customerService;
    private static final Set<Integer> ALLOWED_INSTALLMENTS = Set.of(6, 9, 12, 24);
    private static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");
    private static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");


    public Loan create(LoanRequest loanRequest) throws IllegalAccessException {
        Long customerId = loanRequest.customerId();
        Customer customer = customerService.findById(customerId);
        validateAmount(loanRequest.amount(), customer);
        validateInstallmentCount(loanRequest.numberOfInstallments());
        validateInterestRate(loanRequest.interestRate());
        Loan loan = Loan.builder().
                customer(customer).
                loanAmount(loanRequest.amount()).
                numberOfInstallment(loanRequest.numberOfInstallments()).
                createDate(LocalDate.now()).
                isPaid(false).
                build();

        List<LoanInstallment> loanInstallments = prepareLoanInstallments(loanRequest, loan);
        loan.setInstallments(loanInstallments);

        return loanRepository.save(loan);
    }


    private void validateInstallmentCount(int numberOfInstallments) {
        if (!ALLOWED_INSTALLMENTS.contains(numberOfInstallments)) {
            throw new IllegalArgumentException("Installment count must be one of: 6, 9, 12, or 24");
        }
    }

    private void validateAmount(BigDecimal loanAmount, Customer customer) {
        BigDecimal creditLimit = customer.getCreditLimit();
        BigDecimal usedCreditLimit = customer.getUsedCreditLimit();
        BigDecimal totalUsage = usedCreditLimit.add(loanAmount);

        if (creditLimit.compareTo(totalUsage) < 0) {
            throw new RuntimeException("Insufficient credit limit for this loan");
        }
    }

    private void validateInterestRate(BigDecimal interestRate) {
        if (interestRate == null
                || interestRate.compareTo(MIN_INTEREST_RATE) < 0
                || interestRate.compareTo(MAX_INTEREST_RATE) > 0) {
            throw new IllegalArgumentException("Interest rate must be between 0.1 and 0.5 inclusive");
        }
    }

    private List<LoanInstallment> prepareLoanInstallments(LoanRequest loanRequest, Loan loan) {
        BigDecimal totalAmount = loanRequest.amount().multiply(BigDecimal.ONE.add(loanRequest.interestRate()));
        int numberOfInstallments = loanRequest.numberOfInstallments();
        BigDecimal installmentAmount = totalAmount
                .divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

        BigDecimal sumOfPreviousAmounts = BigDecimal.ZERO;
        List<LoanInstallment> installments = new ArrayList<>();

        for (int i = 0; i < numberOfInstallments; i++) {
            LoanInstallment loanInstallment = new LoanInstallment();
            BigDecimal amount;

            if (i == numberOfInstallments - 1) {
                amount = totalAmount.subtract(sumOfPreviousAmounts);
            } else {
                amount = installmentAmount;
                sumOfPreviousAmounts = sumOfPreviousAmounts.add(amount);
            }

            loanInstallment.setAmount(amount);
            loanInstallment.setPaidAmount(BigDecimal.ZERO);
            loanInstallment.setIsPaid(false);
            loanInstallment.setPaymentDate(null);
            loanInstallment.setLoan(loan);
            LocalDate dueDate = LocalDate.now().plusMonths(i + 1).withDayOfMonth(1);
            loanInstallment.setDueDate(dueDate);
            installments.add(loanInstallment);
        }

        return installments;
    }


    public List<Loan> filterLoans(LoanFilterRequest loanFilterRequest) {
        Specification<Loan> spec = SpecificationBuilder.buildSpecification(loanFilterRequest.getFilterList());
        return loanRepository.findAll(spec).stream().toList();
    }
}
