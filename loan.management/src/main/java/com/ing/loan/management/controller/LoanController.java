package com.ing.loan.management.controller;

import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.filter.LoanFilterRequest;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.request.LoanRequest;
import com.ing.loan.management.response.LoanPaymentResponse;
import com.ing.loan.management.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Create a new loan",
            description = "Creates a new loan for a given customer with the specified amount, interest rate, and installment count.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Loan created successfully",
                            content = @Content(schema = @Schema(implementation = Loan.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Loan> create(@RequestBody LoanRequest request) throws IllegalAccessException {
        log.info("new loan creation {}", request);
        Loan loan = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping("/filter")
    public List<Loan> filter(@RequestBody LoanFilterRequest request) {
        return service.filterLoans(request);
    }

    @PostMapping("/pay")
    public LoanPaymentResponse pay(@RequestBody LoanPayRequest request) {
        return service.pay(request);
    }

    @GetMapping("/list/installments")
    public List<LoanInstallment> findAllInstallmentsByLoanId(@RequestBody Long loanId) {
        return service.findAllInstallmentsByLoanId(loanId);
    }
}

