package com.example.bankcards.exception.transfer;

import java.math.BigDecimal;

public class TransferNegativeAmountException extends TransferException {
    public TransferNegativeAmountException(long sourceCardId, long targetCardId, BigDecimal amount) {
        super(sourceCardId, targetCardId, amount);
    }

    @Override
    protected String buildErrorMessage(long sourceCardId, long targetCardId, BigDecimal amount) {
        return String.format("Transfer error: negative transfer amount for cards' ids %s %s and amount %s",
                sourceCardId, targetCardId, amount);
    }
}
