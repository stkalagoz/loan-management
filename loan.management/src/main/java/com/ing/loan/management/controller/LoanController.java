package com.ing.loan.management.controller;

import com.ing.loan.management.criteria.CriteriaRequest;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.request.LoanRequest;
import com.ing.loan.management.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public void create(@RequestBody LoanRequest request) throws IllegalAccessException {
        log.info("new loan creation {}", request);
        service.create(request);
    }

    @GetMapping("/filter")
    public List<Loan> filterLoans(@RequestBody CriteriaRequest request) {
        return service.filterLoans(request);
    }
}

