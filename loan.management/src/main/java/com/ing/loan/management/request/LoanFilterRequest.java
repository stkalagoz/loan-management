package com.ing.loan.management.request;

import com.ing.loan.management.filter.Filter;

import java.util.List;

public record LoanFilterRequest(Long customerId,List<Filter> filterList) {
}
