package com.example.bankcards.exception;

import lombok.Data;

@Data
public class ApiError {
    private String message;
    private int code;

    public ApiError(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
