package com.example.bankcards.dto.response;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.enums.BankCardStatus;
import com.example.bankcards.util.CardNumberMasker;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class BankCardForUserDto {
    private Long id;
    private String maskedNumber;
    private BigDecimal balance;
    private BankCardStatus status;
    private OffsetDateTime expirationDate;

    public BankCardForUserDto(BankCard card) {
        this.id = card.getCardId();
        this.maskedNumber = CardNumberMasker.maskCardNumber(card.getNumber());
        this.balance = card.getBalance();
        this.status = card.getStatus();
        this.expirationDate = card.getExpirationDate();
    }
}
