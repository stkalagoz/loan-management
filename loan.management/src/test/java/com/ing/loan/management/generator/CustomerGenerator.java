package com.ing.loan.management.generator;

import com.ing.loan.management.entity.Customer;

import java.math.BigDecimal;

public class CustomerGenerator {

    private static final String MOCK_NAME = "Test";
    private static final  BigDecimal CREDIT_LIMIT = BigDecimal.valueOf(100000);
    private static final Long VALID_MOCK_ID = 1L;
    private static final Long INVALID_MOCK_ID = 99L;

    public static Customer getCustomer() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(VALID_MOCK_ID);
        mockCustomer.setCreditLimit(CREDIT_LIMIT);
        mockCustomer.setName(MOCK_NAME);
        mockCustomer.setSurname(MOCK_NAME);
        mockCustomer.setUsedCreditLimit(BigDecimal.ZERO);
        return mockCustomer;
    }

    public static Long getInvalidMockId() {
        return INVALID_MOCK_ID;
    }

    public static Long getValidMockId() {
        return VALID_MOCK_ID;
    }
}
