package com.ing.loan.management.service;

import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository repository;

    public Customer findById(Long customerId) {
        return repository.findById(customerId).orElseThrow();
    }
}
