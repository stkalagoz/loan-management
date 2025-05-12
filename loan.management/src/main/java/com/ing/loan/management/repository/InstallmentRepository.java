package com.ing.loan.management.repository;

import com.ing.loan.management.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<LoanInstallment, Long>, JpaSpecificationExecutor<LoanInstallment> {

    List<LoanInstallment> findAllByLoanId(Long loanId);

}
