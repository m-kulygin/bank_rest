package com.example.bankcards.exception;

import com.example.bankcards.dto.response.ApiError;
import com.example.bankcards.exception.general.BankCardNotActiveException;
import com.example.bankcards.exception.general.BankCardNotFoundException;
import com.example.bankcards.exception.general.BankUserNotFoundException;
import com.example.bankcards.exception.money_transfer.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError handleGenericException(Exception e) {
        return new ApiError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ApiError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ApiError(violation.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return errors;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add(new ApiError(error.getDefaultMessage(), HttpStatus.BAD_REQUEST.value()));
        });
        return errors;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiError handleAccessDeniedException(AccessDeniedException e) {
        return new ApiError("Access denied", HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(TransferDiffOwnersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleTransferDiffOwnersException(TransferDiffOwnersException e) {
        return new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(TransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleTransferException(TransferException e) {
        return new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(TransferNegativeAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleTransferNegativeAmountException(TransferNegativeAmountException e) {
        return new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(TransferNotEnoughException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleTransferNotEnoughException(TransferNotEnoughException e) {
        return new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BankCardNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleBankCardNotActiveException(BankCardNotActiveException e) {
        return new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BankCardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleBankCardNotFoundException(BankCardNotFoundException e) {
        return new ApiError(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(BankCardTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleBankCardTransferException(BankCardTransferException e) {
        return new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BankUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleBankUserNotFoundException(BankUserNotFoundException e) {
        return new ApiError(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }
}
