package sistema_bancario.services.payment.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sistema_bancario.entities.PixTransaction;
import sistema_bancario.entities.Transaction;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.entities.interfaces.PaymentMethod;
import sistema_bancario.entities.interfaces.methods.PixTransactionDTO;
import sistema_bancario.entities.interfaces.methods.TransactionDataDTO;
import sistema_bancario.services.payment.TransactionValidations;

import java.time.Instant;

@Service
public class PixService extends TransactionValidations implements PaymentMethod {
    private static final Logger log = LoggerFactory.getLogger(PixService.class);

    public PixService() {
    }

    @Override
    public TransactionTypeEnum getTransactionType() {
        return TransactionTypeEnum.PIX;
    }

    @Override
    public Transaction processPayment(TransactionDataDTO transactionData) {
        if (!(transactionData instanceof PixTransactionDTO pixDto)) {
            throw new IllegalArgumentException("Dados de transferência inválidos para PIX.");
        }

        String senderAccountId = pixDto.senderAccount().getId();
        String receiverAccountId = pixDto.receiverAccount().getId();

        log.info(
            "event=pix_request senderAccountId={} receiverAccountId={} amount={}",
            senderAccountId,
            receiverAccountId,
            pixDto.amount()
        );

        if (pixDto.receiverAccount().equals(pixDto.senderAccount())) {
            throw new IllegalArgumentException("Não é possível realizar transferência para a própria conta.");
        }

        validateAmount(pixDto.amount());
        validateEnoughBalance(pixDto.senderAccount(), pixDto.amount());

        pixDto.senderAccount().withdraw(pixDto.amount());
        pixDto.receiverAccount().deposit(pixDto.amount());

        PixTransaction pixTransaction = new PixTransaction(null, pixDto.type(), pixDto.amount(), Instant.now(), pixDto.senderAccount(), pixDto.receiverAccount());

        log.info(
            "event=pix_success senderAccountId={} receiverAccountId={} amount={} senderBalanceAfter={} receiverBalanceAfter={}",
            senderAccountId,
            receiverAccountId,
            pixDto.amount(),
            pixDto.senderAccount().getBalance(),
            pixDto.receiverAccount().getBalance()
        );

        return pixTransaction;
    }
}