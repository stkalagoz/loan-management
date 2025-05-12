package com.ing.loan.management.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpecificationBuilder {

    public static <T> Specification<T> buildSpecification(List<Filter> filterList) {
        Specification<T> spec = Specification.where(null);
        for (Filter filter : filterList) {
            spec = spec.and(new GenericSpecification<>(filter));
        }
        return spec;
    }
}
