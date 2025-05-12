package com.ing.loan.management.controller;

import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/installment")
@RequiredArgsConstructor
public class InstallmentController {

    private final InstallmentService service;

    @GetMapping
    public List<LoanInstallment> findAllInstallmentsByLoanId(@RequestBody Long loanId) {
        return service.findAllInstallmentsByLoanId(loanId);
    }
}
