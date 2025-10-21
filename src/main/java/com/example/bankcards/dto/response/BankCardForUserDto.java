package com.example.bankcards.dto.response;

import com.example.bankcards.entity.BankCard;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BankCardForUserDto {
    private Long id;
    private String maskedNumber;
    private BigDecimal balance;

    public BankCardForUserDto(BankCard card) {
        this.id = card.getCardId();
        this.maskedNumber = maskCardNumber(card.getNumber());
        this.balance = card.getBalance();
    }

    private String maskCardNumber(String fullNumber) {
        return "**** **** **** " + fullNumber.substring(fullNumber.length() - 4);
    }
}
