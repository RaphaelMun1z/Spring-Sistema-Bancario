package sistema_bancario.entities;

import jakarta.persistence.*;
import sistema_bancario.entities.enums.TransactionTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "tb_tranactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private TransactionTypeEnum type;

    private BigDecimal amount;

    private LocalDateTime moment;

    @ManyToOne
    @JoinColumn(name = "origin_account_id", nullable = false)
    private Account originAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;
}
