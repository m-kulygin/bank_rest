package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.BankUserRole;

public record BankUserDto(Long id,
                          String firstName,
                          String lastName,
                          BankUserRole role) {
}
