package com.ing.loan.management.service;

import com.ing.loan.management.criteria.CriteriaRequest;
import com.ing.loan.management.criteria.SpecificationBuilder;
import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.repository.LoanRepository;
import com.ing.loan.management.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerService customerService;

    public void create(LoanRequest loanRequest) throws IllegalAccessException {
        Long customerId = loanRequest.customerId();
        Customer customer=customerService.findById(customerId);
        List<LoanInstallment> loanInstallments = calculateLoanInstallmentByLoanAmount(loanRequest.amount());
        Loan loan = Loan.builder().
                customer(customer).
                loanAmount(loanRequest.amount()).
                numberOfInstallment(loanRequest.numberOfInstallments()).
                createDate(LocalDate.now()).
                isPaid(false).
                installments(loanInstallments).
                build();

        loanRepository.save(loan);
    }

    private List<LoanInstallment> calculateLoanInstallmentByLoanAmount(BigDecimal amount) {
        return null;
    }

    public List<Loan> filterLoans(CriteriaRequest criteriaRequest) {
        Specification<Loan> spec = SpecificationBuilder.buildSpecification(criteriaRequest.getCriteriaList());
        return loanRepository.findAll(spec).stream().toList();
    }
}
