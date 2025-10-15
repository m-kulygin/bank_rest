package com.example.bankcards.util;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.BankUser;

public class DtoConverter {
    public static BankCardDto convertBankCardToDto(BankCard bankCard) {
        return new BankCardDto(bankCard.getCardId(),
                bankCard.getNumber(),
                convertBankUserToDto(bankCard.getUser()),
                bankCard.getExpirationDate(),
                bankCard.getStatus(),
                bankCard.getBalance());
    }

    public static BankUserDto convertBankUserToDto(BankUser bankUser) {
        return new BankUserDto(bankUser.getId(),
                bankUser.getFirstName(),
                bankUser.getLastName(),
                bankUser.getRole());
    }
}
