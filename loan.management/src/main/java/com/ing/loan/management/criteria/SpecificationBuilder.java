package com.ing.loan.management.criteria;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpecificationBuilder {

    public static <T> Specification<T> buildSpecification(List<Criteria> criteriaList) {
        Specification<T> spec = Specification.where(null);
        for (Criteria criteria : criteriaList) {
            spec = spec.and(new GenericSpecification<>(criteria));
        }
        return spec;
    }
}
