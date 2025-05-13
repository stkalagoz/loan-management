package com.ing.loan.management.service;

import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.entity.Loan;
import com.ing.loan.management.entity.LoanInstallment;
import com.ing.loan.management.filter.Filter;
import com.ing.loan.management.filter.LoanFilterRequest;
import com.ing.loan.management.generator.CustomerGenerator;
import com.ing.loan.management.generator.LoanGenerator;
import com.ing.loan.management.repository.LoanRepository;
import com.ing.loan.management.request.LoanPayRequest;
import com.ing.loan.management.request.LoanCreateRequest;
import com.ing.loan.management.response.LoanPaymentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
		LoanCreateRequest request = LoanGenerator.getLoanRequest();
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
		LoanPayRequest loanPayRequest = LoanGenerator.getLoanPayRequest();
		Loan loan = LoanGenerator.getLoan();

		List<LoanInstallment> installments = LoanGenerator.getLoanInstallments();
		loan.setInstallments(installments);

		when(repository.findById(loan.getId())).thenReturn(Optional.of(loan));
		when(repository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

		LoanPaymentResponse response = service.pay(loanPayRequest);

		assertThat(response.installmentsPaid()).isEqualTo(1);
		assertThat(response.loanCompletelyPaid()).isTrue();

		verify(repository).save(loan);
	}

	@Test
	void findAllInstallmentsByLoanId_shouldReturnInstallments_whenLoanExists() {
		Long loanId = LoanGenerator.getValidLoanId();
		Loan loan = LoanGenerator.getLoan();
		List<LoanInstallment> installments = loan.getInstallments();

		when(repository.findById(loanId)).thenReturn(Optional.of(loan));

		List<LoanInstallment> result = service.findAllInstallmentsByLoanId(loanId);

		assertThat(result).hasSize(2);
		assertThat(result).containsExactlyElementsOf(installments);
		verify(repository).findById(loanId);
	}

	@Test
	void findAllInstallmentsByLoanId_shouldThrowException_whenLoanDoesNotExist() {
		Long loanId = LoanGenerator.getInvalidLoanId();
		when(repository.findById(loanId)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.findAllInstallmentsByLoanId(loanId));
		verify(repository).findById(loanId);
	}

	@Test
	void filterLoans_shouldReturnLoansMatchingFilters() {
		Filter filter = LoanGenerator.getFilter();
		LoanFilterRequest filterRequest = new LoanFilterRequest();
		filterRequest.setFilterList(List.of(filter));
		Loan mockLoan = LoanGenerator.getLoan();

		when(repository.findAll(any(Specification.class))).thenReturn(List.of(mockLoan));

		List<Loan> result = service.filterLoans(filterRequest);

		assertThat(result).hasSize(1);
		verify(repository, times(1)).findAll(any(Specification.class));
	}



}