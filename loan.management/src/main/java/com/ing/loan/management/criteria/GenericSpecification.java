package com.ing.loan.management.criteria;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private final Criteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return switch (criteria.getOperation()) {
            case "eq" -> cb.equal(root.get(criteria.getKey()), criteria.getValue());
            case "gt" -> cb.greaterThan(root.get(criteria.getKey()), (Comparable) criteria.getValue());
            case "lt" -> cb.lessThan(root.get(criteria.getKey()), (Comparable) criteria.getValue());
            case "like" ->
                    cb.like(cb.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            default -> throw new IllegalArgumentException("Unsupported operation: " + criteria.getOperation());
        };
    }
}
