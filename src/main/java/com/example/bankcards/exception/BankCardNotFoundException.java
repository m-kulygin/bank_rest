package com.example.bankcards.exception;

public class BankCardNotFoundException extends RuntimeException {
    private BankCardNotFoundException(String message) {
        super(message);
    }

    public static BankCardNotFoundException byId(Long bankCardId) {
        return new BankCardNotFoundException("Could not find bank card with id '%s'".formatted(bankCardId));
    }
}
