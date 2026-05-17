package sistema_bancario.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sistema_bancario.dtos.req.AccountReqDTO;
import sistema_bancario.dtos.req.CustomerReqDTO;
import sistema_bancario.dtos.req.TransactionReqDTO;
import sistema_bancario.dtos.res.AccountDetailsResDTO;
import sistema_bancario.dtos.res.CustomerDetailsResDTO;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.repositories.AccountRepository;
import sistema_bancario.services.AccountService;
import sistema_bancario.services.CustomerService;
import sistema_bancario.services.payment.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@Profile({"dev", "test"})
public class TestConfig implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TestConfig.class);

    private final CustomerService customerService;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final AccountService accountService;

    public TestConfig(
        CustomerService customerService, AccountRepository accountRepository,
        TransactionService transactionService, AccountService accountService
    ) {
        this.customerService = customerService;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            CustomerDetailsResDTO customer01 = customerService.createCustomer(
                new CustomerReqDTO(
                    "Raphael Muniz",
                    "(13) 99999-9999",
                    "raphaelmuniz@gmail.com",
                    "123123",
                    "123.123.123-12",
                    LocalDate.of(2004, 10, 22)
                )
            );

            CustomerDetailsResDTO customer02 = customerService.createCustomer(
                new CustomerReqDTO(
                    "Neymar Jr",
                    "(11) 99999-9999",
                    "neymar@gmail.com",
                    "123123",
                    "321.321.321-21",
                    LocalDate.of(2000, 1, 1)
                )
            );

            AccountDetailsResDTO account01 = accountService.createAccount(new AccountReqDTO(customer01.cpf()));
            AccountDetailsResDTO account02 = accountService.createAccount(new AccountReqDTO(customer02.cpf()));

            accountService.consultAccountDetails(account01.id());
            accountService.consultAccountDetails(account02.id());

            transactionService.deposit(BigDecimal.valueOf(9000.00), account01.id());
            transactionService.withdraw(BigDecimal.valueOf(200.00), account01.id());

            accountService.consultAccountBalance(account02.id());
            accountService.consultAccountBalance(account01.id());

            transactionService.realizePayment(
                new TransactionReqDTO(
                    TransactionTypeEnum.PIX,
                    BigDecimal.valueOf(1200),
                    account01.id(),
                    account02.id()
                )
            );

            transactionService.realizePayment(
                new TransactionReqDTO(
                    TransactionTypeEnum.CREDIT_CARD,
                    BigDecimal.valueOf(110),
                    account02.id(),
                    account01.id()
                )
            );

            accountService.consultAccountBalance(account02.id());
            accountService.consultAccountBalance(account01.id());
        } catch (Exception e) {
            Throwable root = e;

            while (root.getCause() != null) {
                root = root.getCause();
            }

            log.error(
                "event=startup_error type={} message={}",
                root.getClass().getSimpleName(),
                root.getMessage()
            );
        }
    }
}