package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.BankCardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "bankcard")
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.EAGER) // !!!!!!!???????????
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private BankUser user;

    @Column(name = "expiration_date", nullable = false)
    private OffsetDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class) // https://www.baeldung.com/java-enums-jpa-postgresql
    @Column(name = "status", nullable = false)
    private BankCardStatus status;

    @Column(name = "balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal balance;

    public BankCard(String number,
                    BankUser user,
                    OffsetDateTime expirationDate,
                    BankCardStatus status,
                    BigDecimal balance) {
        this.number = number;
        this.user = user;
        this.expirationDate = expirationDate;
        this.status = status;
        this.balance = balance;
    }

}
