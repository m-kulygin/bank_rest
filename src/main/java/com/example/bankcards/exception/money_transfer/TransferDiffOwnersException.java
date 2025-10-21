package com.example.bankcards.exception.money_transfer;

import java.math.BigDecimal;

public class TransferDiffOwnersException extends TransferException {
    public TransferDiffOwnersException(long sourceCardId, long targetCardId, BigDecimal amount) {
        super(sourceCardId, targetCardId, amount);
    }

    @Override
    protected String buildErrorMessage(long sourceCardId, long targetCardId, BigDecimal amount) {
        return String.format("Transfer error: different owners for cards' ids %s %s",
                sourceCardId, targetCardId);
    }
}
