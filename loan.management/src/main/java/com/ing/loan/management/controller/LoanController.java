package com.ing.loan.management.controller;

import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.filter.Filter;
import com.ing.loan.management.request.LoanCreateRequest;
import com.ing.loan.management.request.LoanFilterRequest;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.response.LoanPaymentResponse;
import com.ing.loan.management.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Loan> create(@RequestBody LoanCreateRequest request) throws IllegalAccessException {
        log.info("new loan creation {}", request);
        Loan loan = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping("/filter")
    public List<Loan> filter(@RequestBody LoanFilterRequest request) {
        log.info("loans filtering {}",request);
        return service.filterLoans(request);
    }

    @PostMapping("/pay")
    public LoanPaymentResponse pay(@RequestBody LoanPayRequest request) {
        log.info("loan paying {}", request);
        return service.pay(request);
    }

    @GetMapping("/list/installments")
    public List<LoanInstallment> findAllInstallmentsByLoanId(@RequestBody Long loanId) {
        log.info("loan installment filtering {}", loanId);
        return service.findAllInstallmentsByLoanId(loanId);
    }
}

