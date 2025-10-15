package com.example.bankcards.controller;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.repository.BankCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankCardController {

    private final BankCardRepository repository;

    @Autowired
    public BankCardController(BankCardRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/bank_cards")
    public List<BankCard> findAllBankCards() {
        return repository.findAll();
    }
}
