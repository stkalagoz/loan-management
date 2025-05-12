package com.ing.loan.management.service;

import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.repository.InstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstallmentService {

    private final InstallmentRepository repository;

    public List<LoanInstallment> findAllInstallmentsByLoanId(Long loanId) {
        return repository.findAllByLoanId(loanId);
    }
}
