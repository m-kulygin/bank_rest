package com.example.bankcards.controller;

import com.example.bankcards.entity.BankUser;
import com.example.bankcards.repository.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankUserController {

    private final BankUserRepository repository;

    @Autowired
    public BankUserController(BankUserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/bank_users")
    public List<BankUser> findAllBankUsers() {
        return repository.findAll();
    }
}
