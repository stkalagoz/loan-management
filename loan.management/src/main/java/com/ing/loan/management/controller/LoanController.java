package com.ing.loan.management.controller;

import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.request.LoanCreateRequest;
import com.ing.loan.management.request.LoanFilterRequest;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.response.LoanPaymentResponse;
import com.ing.loan.management.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Loan API", description = "Operations related to loans")
public class LoanController {

    private final LoanService service;

    @Operation(
            summary = "Create a new loan",
            description = "Creates a loan for a specific customer based on the provided request"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Loan> create(@RequestBody LoanCreateRequest request) throws IllegalAccessException {
        log.info("new loan creation {}", request);
        Loan loan = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @Operation(
            summary = "Filter loans",
            description = "Filters loans based on dynamic search criteria provided in the request."
    )
    @GetMapping("/filter")
    public List<Loan> filter(@RequestBody LoanFilterRequest request) {
        log.info("loans filtering {}",request);
        return service.filterLoans(request);
    }

    @Operation(
            summary = "Pay loan installments",
            description = "Pays one or more installments for a specific loan based on the payment amount and date."
    )
    @PostMapping("/pay")
    public LoanPaymentResponse pay(@RequestBody LoanPayRequest request) {
        log.info("loan paying {}", request);
        return service.pay(request);
    }

    @Operation(
            summary = "List loan installments",
            description = "Returns all installments for a given loan ID."
    )
    @GetMapping("/list/installments")
    public List<LoanInstallment> findAllInstallmentsByLoanId(@RequestBody Long loanId) {
        log.info("loan installment filtering {}", loanId);
        return service.findAllInstallmentsByLoanId(loanId);
    }
}

