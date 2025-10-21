package com.example.bankcards.exception;

public class BankUserNotFoundException extends RuntimeException {

    private BankUserNotFoundException(String message) {
        super(message);
    }

    public static BankUserNotFoundException byId(Long bankUserId) {
        return new BankUserNotFoundException("Could not find bank user with id '%s'".formatted(bankUserId));
    }
}
