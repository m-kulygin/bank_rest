package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.repository.BankUserRepository;
import com.example.bankcards.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankUserService {
    private final BankUserRepository bankUserRepository;

    @Autowired
    public BankUserService(BankUserRepository bankUserRepository) {
        this.bankUserRepository = bankUserRepository;
    }

    @Transactional(readOnly = true)
    public List<BankUserDto> getAll() {
        List<BankUser> cards = bankUserRepository.findAll();
        return cards.stream()
                .map(DtoConverter::convertBankUserToDto)
                .toList();
    }
}
