package com.example.bankcards.exception.money_transfer;

import java.math.BigDecimal;

public class TransferNotEnoughException extends TransferException {
    public TransferNotEnoughException(long sourceCardId, long targetCardId, BigDecimal amount) {
        super(sourceCardId, targetCardId, amount);
    }

    @Override
    protected String buildErrorMessage(long sourceCardId, long targetCardId, BigDecimal amount) {
        return String.format("Transfer error: not enough money for source card id %s to transfer to target card id %s, amount %s",
                sourceCardId, targetCardId, amount);
    }
}
