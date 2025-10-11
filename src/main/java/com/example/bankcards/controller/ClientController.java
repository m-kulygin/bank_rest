package com.example.bankcards.controller;

import com.example.bankcards.entity.Client;
import com.example.bankcards.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController {

    private final ClientRepository repository;

    @Autowired
    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/clients")
    public List<Client> findAllClients() {
        return repository.findAll();
    }
}
