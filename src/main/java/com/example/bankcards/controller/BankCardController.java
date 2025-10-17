package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.service.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankCardController {

    private final BankCardService bankCardService;

    @Autowired
    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @GetMapping("/bank_cards")
    @PreAuthorize("hasRole('CLIENT')")
    public List<BankCardDto> findAllBankCards() {
        return bankCardService.getAll();
    }
}
