package com.ing.loan.management.filter;

import com.ing.loan.management.request.LoanFilterRequest;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {

    public static <T> Specification<T> buildSpecificationForFilterLoan(LoanFilterRequest request) {
        Specification<T> spec = Specification.where(null);
        Long customerId = request.customerId();

        spec = getTSpecificationForCustomer(customerId, spec);

        for (Filter filter : request.filterList()) {
            spec = spec.and(new GenericSpecification<>(filter));
        }

        return spec;
    }

    private static <T> Specification<T> getTSpecificationForCustomer(Long customerId, Specification<T> spec) {
        if (customerId != null) {
            Filter customerFilter = new Filter();
            customerFilter.setKey("customer.id");
            customerFilter.setOperation("eq");
            customerFilter.setValue(customerId);
            spec = spec.and(new GenericSpecification<>(customerFilter));
        } else {
            throw new IllegalArgumentException("Customer id is required");
        }
        return spec;
    }

}
