package sistema_bancario.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import sistema_bancario.dtos.req.AccountReqDTO;
import sistema_bancario.dtos.res.AccountDetailsResDTO;
import sistema_bancario.entities.Account;
import sistema_bancario.entities.users.Customer;
import sistema_bancario.repositories.AccountRepository;
import sistema_bancario.repositories.CustomerRepository;
import sistema_bancario.services.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    public AccountService(AccountRepository accountRepository, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
    }

    public AccountDetailsResDTO createAccount(AccountReqDTO req){
        log.info(
                "event=create_account_request"
        );

        Customer customer = customerService.findCustomerByCpf(req.customer_cpf());

        Account newAccount = new Account();
        newAccount.setCustomer(customer);
        newAccount.setBalance(BigDecimal.valueOf(0.00));

        Account savedAccount = accountRepository.save(newAccount);

        return new AccountDetailsResDTO(savedAccount.getId(), savedAccount.getBalance(), req.customer_cpf());
    }

    public BigDecimal consultAccountBalance(String accountId) {
        log.info(
                "event=consult_account_balance_request accountId={}",
                accountId
        );

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn(
                            "event=consult_account_balance_failed reason=account_not_found accountId={}",
                            accountId
                    );

                    return new ResourceNotFoundException("Conta não encontrada.");
                });

        log.info(
                "event=consult_account_balance_success accountId={} balance={}",
                accountId,
                account.getBalance()
        );

        return account.getBalance();
    }

    public AccountDetailsResDTO consultAccountDetails(String accountId) {
        log.info(
                "event=consult_account_details_request accountId={}",
                accountId
        );

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn(
                            "event=consult_account_details_failed reason=account_not_found accountId={}",
                            accountId
                    );

                    return new ResourceNotFoundException("Conta não encontrada.");
                });

        log.info(
                "event=consult_account_details_success accountId={}",
                accountId
        );

        return new AccountDetailsResDTO(accountId, account.getBalance(), account.getCustomer().getId());
    }
}
