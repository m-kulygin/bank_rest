package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record BankCardSearchCriteria(
        @Schema(description = "Минимальный баланс", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        BigDecimal balanceFrom,
        @Schema(description = "Максимальный баланс", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        BigDecimal balanceTo,
        @Schema(description = "Начало периода срока действия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        OffsetDateTime expirationDateStart,
        @Schema(description = "Окончание периода срока действия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        OffsetDateTime expirationDateEnd,
        @Schema(description = "Статусы карт", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<String> statuses) {
}
