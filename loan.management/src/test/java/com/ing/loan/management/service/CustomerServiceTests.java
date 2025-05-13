package com.ing.loan.management.service;

import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.generator.CustomerGenerator;
import com.ing.loan.management.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

	@Mock
	private CustomerRepository repository;

	@InjectMocks
	private CustomerService service;

	@Test
	void findById_shouldReturnCustomer_whenCustomerExists() {
		Customer mockCustomer = CustomerGenerator.getCustomer();
		Long customerId = mockCustomer.getId();

		when(repository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

		Customer result = service.findById(customerId);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(customerId);
		verify(repository, times(1)).findById(customerId);
	}

	@Test
	void findById_shouldThrowException_whenCustomerDoesNotExist() {
		Long customerId = CustomerGenerator.getInvalidMockId();
		when(repository.findById(customerId)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.findById(customerId));
		verify(repository, times(1)).findById(customerId);
	}
}
