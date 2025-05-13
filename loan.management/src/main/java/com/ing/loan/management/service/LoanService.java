package com.ing.loan.management.service;

import com.ing.loan.management.filter.SpecificationBuilder;
import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.pattern.strategy.AdjustmentStrategyFactory;
import com.ing.loan.management.pattern.strategy.PaymentAdjustmentStrategy;
import com.ing.loan.management.repository.LoanRepository;
import com.ing.loan.management.request.LoanFilterRequest;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.request.LoanCreateRequest;
import com.ing.loan.management.response.LoanPaymentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerService customerService;
    private static final Set<Integer> ALLOWED_INSTALLMENTS = Set.of(6, 9, 12, 24);
    private static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");
    private static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");
    private final AdjustmentStrategyFactory adjustmentStrategyFactory = new AdjustmentStrategyFactory(); // or inject via @Component if desired

    public Loan create(LoanCreateRequest loanCreateRequest) throws IllegalAccessException {
        Long customerId = loanCreateRequest.customerId();
        Customer customer = customerService.findById(customerId);
        validateAmount(loanCreateRequest.amount(), customer);
        validateInstallmentCount(loanCreateRequest.numberOfInstallments());
        validateInterestRate(loanCreateRequest.interestRate());
        Loan loan = Loan.builder().
                customer(customer).
                loanAmount(loanCreateRequest.amount()).
                numberOfInstallment(loanCreateRequest.numberOfInstallments()).
                createDate(LocalDate.now()).
                isPaid(false).
                build();

        List<LoanInstallment> loanInstallments = prepareLoanInstallments(loanCreateRequest, loan);
        loan.setInstallments(loanInstallments);

        customerService.updateCreditLimit(customerId, loanCreateRequest.amount());

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

    private List<LoanInstallment> prepareLoanInstallments(LoanCreateRequest loanCreateRequest, Loan loan) {
        BigDecimal totalAmount = loanCreateRequest.amount().multiply(BigDecimal.ONE.add(loanCreateRequest.interestRate()));
        int numberOfInstallments = loanCreateRequest.numberOfInstallments();
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
        Specification<Loan> spec = SpecificationBuilder.buildSpecificationForFilterLoan(loanFilterRequest);
        return loanRepository.findAll(spec).stream().toList();
    }

    public LoanPaymentResponse pay(LoanPayRequest request) {
        Long loanId = request.loanId();
        BigDecimal incomingPayment = request.incomingPayment();
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        List<LoanInstallment> installments = loan.getInstallments();

        installments.sort(Comparator.comparing(LoanInstallment::getDueDate));
        LocalDate today = LocalDate.now();
        LocalDate maxPayableDate = today.withDayOfMonth(1).plusMonths(3);

        int installmentsPaid = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;

        for (LoanInstallment installment : installments) {
            if (!isPayable(installment, maxPayableDate)) continue;

            BigDecimal adjustedAmount = calculateAdjustedAmount(installment, today);

            if (incomingPayment.compareTo(adjustedAmount) >= 0) {
                applyPayment(installment, adjustedAmount, today);
                incomingPayment = incomingPayment.subtract(adjustedAmount);
                totalPaid = totalPaid.add(adjustedAmount);
                installmentsPaid++;
            } else {
                break;
            }
        }

        finalizeLoanStatus(loan);
        loanRepository.save(loan);

        return new LoanPaymentResponse(installmentsPaid, totalPaid, loan.getIsPaid());
    }

    private boolean isPayable(LoanInstallment installment, LocalDate maxPayableDate) {
        return !installment.getIsPaid() && !installment.getDueDate().isAfter(maxPayableDate);
    }

    private BigDecimal calculateAdjustedAmount(LoanInstallment installment, LocalDate today) {
        BigDecimal baseAmount = installment.getAmount();
        long daysDiff = ChronoUnit.DAYS.between(today, installment.getDueDate());

        PaymentAdjustmentStrategy strategy = adjustmentStrategyFactory.getStrategy(daysDiff);
        return strategy.adjust(baseAmount, Math.abs(daysDiff));
    }

    private void applyPayment(LoanInstallment installment, BigDecimal paidAmount, LocalDate today) {
        installment.setPaidAmount(paidAmount);
        installment.setIsPaid(true);
        installment.setPaymentDate(today);
    }

    private void finalizeLoanStatus(Loan loan) {
        boolean allPaid = loan.getInstallments().stream().allMatch(LoanInstallment::getIsPaid);
        loan.setIsPaid(allPaid);
    }

    public List<LoanInstallment> findAllInstallmentsByLoanId(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        return loan.getInstallments();
    }

}
