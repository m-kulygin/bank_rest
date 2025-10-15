package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankCardService {
    private final BankCardRepository bankCardRepository;

    @Autowired
    public BankCardService(BankCardRepository bankCardRepository) {
        this.bankCardRepository = bankCardRepository;
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        List<BankCard> cards = bankCardRepository.findAll();
        return cards.stream()
                .map(DtoConverter::convertBankCardToDto)
                .toList();
    }
}
