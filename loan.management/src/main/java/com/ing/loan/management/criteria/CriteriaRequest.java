package com.ing.loan.management.criteria;

import lombok.Data;

import java.util.List;

@Data
public class CriteriaRequest {
    private List<Criteria> criteriaList;
}
