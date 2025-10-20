package com.example.bankcards.exception;

public class BankUserLoginAlreadyExistsException extends RuntimeException {
    private BankUserLoginAlreadyExistsException(String message) {
        super(message);
    }

    public static BankUserLoginAlreadyExistsException byLogin(String login) {
        return new BankUserLoginAlreadyExistsException("Bank user already exists with login '%s'".formatted(login));
    }
}
