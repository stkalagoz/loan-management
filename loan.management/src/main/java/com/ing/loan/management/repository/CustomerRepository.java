package com.ing.loan.management.repository;

import com.ing.loan.management.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Loan, Long> {

}
