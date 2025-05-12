package com.ing.loan.management.filter;

import lombok.Data;

@Data
public class Filter {
    private String key;
    private Object value;
    private String operation;
}
