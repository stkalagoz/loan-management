package com.ing.loan.management.service;

import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository repository;

    public Customer findById(Long customerId) {
        return repository.findById(customerId).orElseThrow();
    }

    public void updateCreditLimit(Long id, BigDecimal loanAmount) {
        repository.findById(id).ifPresent(customer -> {
            BigDecimal usedCreditLimit = customer.getUsedCreditLimit().add(loanAmount);
            customer.setUsedCreditLimit(usedCreditLimit);
            repository.saveAndFlush(customer);
        });
    }
}
