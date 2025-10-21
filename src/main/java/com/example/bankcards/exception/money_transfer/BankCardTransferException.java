package com.example.bankcards.exception.money_transfer;

import java.math.BigDecimal;

public class BankCardTransferException extends RuntimeException {
    private BankCardTransferException(String message) {
        super(message);
    }

    public static BankCardTransferException byIds(Long sourceId, Long targetId, BigDecimal amount) {
        return new BankCardTransferException(("Error while transferring money between cards with ids '%s', '%s'" +
                " and amount '%s'. Cards must have same owner").formatted(sourceId, targetId, amount));
    }
}
