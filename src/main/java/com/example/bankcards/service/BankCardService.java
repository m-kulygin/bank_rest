package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.enums.BankCardStatus;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BankCardService {
    private final BankCardRepository bankCardRepository;
    private final BankUserService bankUserService;

    @Autowired
    public BankCardService(BankCardRepository bankCardRepository, BankUserService bankUserService) {
        this.bankCardRepository = bankCardRepository;
        this.bankUserService = bankUserService;
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        List<BankCard> cards = bankCardRepository.findAll();
        return cards.stream()
                .map(DtoConverter::convertBankCardToDto)
                .toList();
    }

    @Transactional
    public BankCardDto createCardByUserId(Long userId) {
        BankUser user = bankUserService.getUserOrThrow(userId);
        BankCard card = new BankCard("0000000000000000",
                user,
                OffsetDateTime.now(),
                BankCardStatus.ACTIVE,
                new BigDecimal(0));
        return DtoConverter.convertBankCardToDto(bankCardRepository.save(card));
    }
}
