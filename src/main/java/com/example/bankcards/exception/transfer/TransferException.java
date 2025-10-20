package com.example.bankcards.exception.transfer;

import java.math.BigDecimal;

public abstract class TransferException extends RuntimeException {

    private final long sourceCardId;
    private final long targetCardId;
    private final BigDecimal amount;

    protected TransferException(long sourceCardId, long targetCardId, BigDecimal amount) {
        super();
        this.sourceCardId = sourceCardId;
        this.targetCardId = targetCardId;
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return buildErrorMessage(this.sourceCardId, this.targetCardId, this.amount);
    }

    protected abstract String buildErrorMessage(long sourceCardId, long targetCardId, BigDecimal amount);

}