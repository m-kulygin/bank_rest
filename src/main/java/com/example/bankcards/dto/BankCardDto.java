package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.BankCardStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record BankCardDto (Long cardId,
                           String number,
                           BankUserDto user,
                           OffsetDateTime expirationDate,
                           BankCardStatus status,
                           BigDecimal balance) {
}
