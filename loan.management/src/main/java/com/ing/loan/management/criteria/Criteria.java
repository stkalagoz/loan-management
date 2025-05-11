package com.ing.loan.management.criteria;

import lombok.Data;

@Data
public class Criteria {
    private String key;
    private Object value;
    private String operation;
}
