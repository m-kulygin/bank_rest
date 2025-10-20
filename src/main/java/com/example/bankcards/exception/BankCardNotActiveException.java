package com.example.bankcards.exception;

public class BankCardNotActiveException extends RuntimeException {
    private BankCardNotActiveException(String message) {
        super(message);
    }

    public static BankCardNotActiveException byId(Long bankCardId) {
        return new BankCardNotActiveException("Card not active with id '%s'".formatted(bankCardId));
    }
}
