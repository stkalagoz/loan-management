package com.ing.loan.management.service;

import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.generator.CustomerGenerator;
import com.ing.loan.management.generator.LoanGenerator;
import com.ing.loan.management.repository.LoanRepository;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.request.LoanRequest;
import com.ing.loan.management.response.LoanPaymentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoanServiceTests {

	@Mock
	private LoanRepository repository;

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private LoanService service;

	@Test
	void create_shouldCreateLoanAndUpdateCreditLimit() throws IllegalAccessException {
		LoanRequest request = LoanGenerator.getLoanRequest();
		Customer customer = CustomerGenerator.getCustomer();
		Long customerId = CustomerGenerator.getValidMockId();
		BigDecimal amount=LoanGenerator.getLoanAmount();
		int installments=LoanGenerator.getNumberOfInstallment();

		when(customerService.findById(customerId)).thenReturn(customer);
		when(repository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Loan result = service.create(request);

		assertThat(result).isNotNull();
		assertThat(result.getLoanAmount()).isEqualByComparingTo(amount);
		assertThat(result.getInstallments()).hasSize(installments);
		verify(customerService, times(1)).updateCreditLimit(customerId, amount);
		verify(repository, times(1)).save(any(Loan.class));
	}

	@Test
	void pay_shouldMarkInstallmentsPaidAndReturnResponse() {
		// Arrange
		LoanPayRequest loanPayRequest = LoanGenerator.getLoanPayRequest(); // assumes loanId and incomingPayment
		Loan loan = LoanGenerator.getLoan(); // must include unpaid installments

		// Get and attach installments directly to the loan
		List<LoanInstallment> installments = LoanGenerator.getLoanInstallments(); // assumes list of 2–3 unpaid installments
		loan.setInstallments(installments);

		when(repository.findById(loan.getId())).thenReturn(Optional.of(loan));
		when(repository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		LoanPaymentResponse response = service.pay(loanPayRequest);

		// Assert
		assertThat(response.totalPaid()).isEqualTo(BigDecimal.valueOf(5000));
		assertThat(response.installmentsPaid()).isEqualTo(1);
		assertThat(response.loanCompletelyPaid()).isTrue(); // assuming one unpaid remains

		verify(repository).save(loan);
	}




}