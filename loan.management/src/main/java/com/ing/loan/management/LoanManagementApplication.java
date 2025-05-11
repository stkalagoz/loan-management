package com.ing.loan.management;

import com.ing.loan.management.entity.Customer;
import com.ing.loan.management.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class LoanManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanManagementApplication.class, args);
	}


	@Bean
	public CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			for (int i = 1; i <= 20; i++) {
				BigDecimal creditLimit = BigDecimal.valueOf(100000*i);
				customerRepository.save(
						Customer.builder().
								creditLimit(creditLimit)
								.usedCreditLimit(BigDecimal.valueOf(0))
								.name("TestName"+i)
								.surname("TestSurname"+i)
								.build()
				);
			}
		};
	}
}
