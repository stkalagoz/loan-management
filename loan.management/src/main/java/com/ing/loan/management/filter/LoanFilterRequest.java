package com.ing.loan.management.filter;

import lombok.Data;

import java.util.List;

@Data
public class LoanFilterRequest {
    private List<Filter> filterList;
}
