package sistema_bancario.services.payment.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sistema_bancario.entities.CreditCardTransaction;
import sistema_bancario.entities.PixTransaction;
import sistema_bancario.entities.Transaction;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.entities.interfaces.PaymentMethod;
import sistema_bancario.entities.interfaces.methods.CreditCardTransactionDTO;
import sistema_bancario.entities.interfaces.methods.PixTransactionDTO;
import sistema_bancario.entities.interfaces.methods.TransactionDataDTO;
import sistema_bancario.services.payment.TransactionValidations;

import java.time.Instant;

@Service
public class CreditCardService extends TransactionValidations implements PaymentMethod {
    private static final Logger log = LoggerFactory.getLogger(CreditCardService.class);

    public CreditCardService() {
    }

    @Override
    public TransactionTypeEnum getTransactionType() {
        return TransactionTypeEnum.CREDIT_CARD;
    }

    @Override
    public Transaction processPayment(TransactionDataDTO transactionData) {
        if (!(transactionData instanceof CreditCardTransactionDTO creditCardDto)) {
            throw new IllegalArgumentException("Dados de transferência inválidos para Cartão de Crédito.");
        }

        String senderAccountId = creditCardDto.senderAccount().getId();
        String receiverAccountId = creditCardDto.receiverAccount().getId();

        log.info(
            "event=credit_card_request senderAccountId={} receiverAccountId={} amount={}",
            senderAccountId,
            receiverAccountId,
            creditCardDto.amount()
        );

        if (creditCardDto.receiverAccount().equals(creditCardDto.senderAccount())) {
            throw new IllegalArgumentException("Não é possível realizar transferência para a própria conta.");
        }

        validateAmount(creditCardDto.amount());
        validateEnoughCredit(creditCardDto.senderAccount(), creditCardDto.amount());

        creditCardDto.senderAccount().consumeCreditLimit(creditCardDto.amount());
        creditCardDto.receiverAccount().deposit(creditCardDto.amount());

        CreditCardTransaction creditCardTransaction = new CreditCardTransaction(
            null,
            creditCardDto.type(),
            creditCardDto.amount(),
            Instant.now(),
            creditCardDto.senderAccount(),
            creditCardDto.receiverAccount()
        );

        log.info(
            "event=credit_card_success senderAccountId={} receiverAccountId={} amount={} senderCreditLimitAfter={} receiverBalanceAfter={}",
            senderAccountId,
            receiverAccountId,
            creditCardDto.amount(),
            creditCardDto.senderAccount().getAvailableLimit(),
            creditCardDto.receiverAccount().getBalance()
        );

        return creditCardTransaction;
    }
}