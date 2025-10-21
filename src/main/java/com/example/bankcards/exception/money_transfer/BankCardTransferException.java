package com.example.bankcards.exception.money_transfer;

public class BankCardTransferException extends RuntimeException {
    private BankCardTransferException(String message) {
        super(message);
    }
}
