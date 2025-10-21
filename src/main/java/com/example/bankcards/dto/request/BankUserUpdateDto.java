package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.BankUserRole;
import com.example.bankcards.util.validation.EnumValidator;
import com.example.bankcards.util.validation.RequestStageGroup;
import jakarta.validation.constraints.NotBlank;

public record BankUserUpdateDto(
        @NotBlank(message = "firstName не должен быть пустым") String firstName,
        @NotBlank(message = "lastName не должен быть пустым") String lastName,
        @EnumValidator(
                enumClazz = BankUserRole.class,
                groups = {RequestStageGroup.class}
        ) BankUserRole role) {
}
