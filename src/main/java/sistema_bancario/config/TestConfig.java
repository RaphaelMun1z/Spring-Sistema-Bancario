package sistema_bancario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sistema_bancario.entities.Account;
import sistema_bancario.entities.Transaction;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.entities.users.Customer;
import sistema_bancario.entities.users.User;
import sistema_bancario.repositories.AccountRepository;
import sistema_bancario.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@Profile("dev")
public class TestConfig implements CommandLineRunner {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;

	public TestConfig(
			AccountRepository accountRepository,
			TransactionRepository transactionRepository
	) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Customer customer1 = new Customer(
				null,
				"Raphael",
				"34999999999",
				"raphael@gmail.com",
				"123456",
				"12345678901",
				LocalDate.of(2004, 5, 10)
		);

		Customer customer2 = new Customer(
				null,
				"João",
				"34988888888",
				"joao@gmail.com",
				"123456",
				"98765432100",
				LocalDate.of(2000, 8, 20)
		);

		Account account1 = new Account(
				new BigDecimal("1500.00"),
				customer1
		);

		Account account2 = new Account(
				new BigDecimal("800.00"),
				customer2
		);

		accountRepository.saveAll(Arrays.asList(account1, account2));

		Transaction transaction1 = new Transaction();
		transaction1.setType(TransactionTypeEnum.DEPOSIT);
		transaction1.setAmount(new BigDecimal("500.00"));
		transaction1.setMoment(Instant.now());
		transaction1.setSenderAccount(account1);

		Transaction transaction2 = new Transaction();
		transaction2.setType(TransactionTypeEnum.WITHDRAW);
		transaction2.setAmount(new BigDecimal("200.00"));
		transaction2.setMoment(Instant.now());
		transaction2.setSenderAccount(account2);

		transactionRepository.saveAll(
				Arrays.asList(transaction1, transaction2)
		);
	}
}