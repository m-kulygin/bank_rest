package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.BankCardStatus;
import com.example.bankcards.util.CardNumberMasker;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class BankCardDto {
    private Long cardId;
    private String number;
    private BankUserDto user;
    private OffsetDateTime expirationDate;
    private BankCardStatus status;
    private BigDecimal balance;
    private Boolean blockRequested;

    public BankCardDto(Long cardId,
                       String number,
                       BankUserDto user,
                       OffsetDateTime expirationDate,
                       BankCardStatus status,
                       BigDecimal balance,
                       Boolean blockRequested) {
        this.cardId = cardId;
        this.number = CardNumberMasker.maskCardNumber(number);
        this.user = user;
        this.expirationDate = expirationDate;
        this.status = status;
        this.balance = balance;
        this.blockRequested = blockRequested;
    }
}
