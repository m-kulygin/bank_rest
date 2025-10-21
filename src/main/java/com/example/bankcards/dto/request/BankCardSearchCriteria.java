package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.BankCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record BankCardSearchCriteria(
        @Schema(description = "Минимальный баланс",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                example = "0")
        @PositiveOrZero(message = "balanceFrom должно быть неотрицательным")
        BigDecimal balanceFrom,
        @Schema(description = "Максимальный баланс",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                example = "100000")
        @Positive(message = "balanceTo должно быть положительным")
        BigDecimal balanceTo,
        @Schema(description = "Начало периода срока действия",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                example = "2020-10-21T12:56:48.017Z")
        OffsetDateTime expirationDateStart,
        @Schema(description = "Окончание периода срока действия",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                example = "2030-10-21T12:56:48.017Z")
        OffsetDateTime expirationDateEnd,
        @Schema(description = "Статусы карт",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<BankCardStatus> statuses) {
}
