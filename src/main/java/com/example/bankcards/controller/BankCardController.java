package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.service.BankCardService;
import com.example.bankcards.util.DtoConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bank_cards")
public class BankCardController {

    private final BankCardService bankCardService;

    @Autowired
    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<BankCardDto>> getAllCards() {
        return ResponseEntity.ok(bankCardService.getAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/{userId}")
    public ResponseEntity<BankCardDto> createCardByUserId(@Valid @PathVariable Long userId) {
        BankCardDto createdCard = bankCardService.createCardByUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCard);
    }

//    @GetMapping("/{cardId}")
//    public ResponseEntity<BankCardDto> getCardById(@PathVariable String cardId) { /*...*/ }
//
//    @PatchMapping("/{cardId}/block")
//    public ResponseEntity<Void> blockCard(@PathVariable String cardId) { /*...*/ }
//
//    @PostMapping("/{fromCardId}/transfer/{toCardId}")
//    public ResponseEntity<Void> transferMoney(@PathVariable String fromCardId, @PathVariable String toCardId, @RequestParam BigDecimal amount) { /*...*/ }
}
