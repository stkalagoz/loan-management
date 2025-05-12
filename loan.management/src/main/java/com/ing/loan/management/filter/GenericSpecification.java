package com.ing.loan.management.filter;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private final Filter filter;

    private Path<Object> resolvePath(Root<T> root, String key) {
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return (Path<Object>) path;
        } else {
            return root.get(key);
        }
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path<Object> path = resolvePath(root, filter.getKey());
        return cb.equal(path, filter.getValue());
    }
}
