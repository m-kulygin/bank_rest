package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardForUserDto;
import com.example.bankcards.dto.BankCardSearchCriteria;
import com.example.bankcards.service.BankCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PreAuthorize("hasAuthority('ADMIN')") // Создаёт
    @PostMapping("/users/{userId}")
    public ResponseEntity<BankCardDto> createCardByUserId(@Valid @PathVariable Long userId) {
        BankCardDto createdCard = bankCardService.createCardByUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCard);
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Блокирует
    @PostMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
        bankCardService.blockCard(cardId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Активирует
    @PostMapping("/{cardId}/activate")
    public ResponseEntity<Void> activateCard(@PathVariable Long cardId) {
        bankCardService.activateCard(cardId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Удаляет
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        bankCardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Видит все карты
    @GetMapping("/all")
    public ResponseEntity<List<BankCardDto>> getAllCards() {
        return ResponseEntity.ok(bankCardService.getAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Видит все карты пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankCardDto>> getAllUserCards(@PathVariable Long userId) {
        return ResponseEntity.ok(bankCardService.getAllByUserId(userId));
    }

    @PreAuthorize("hasAuthority('USER')") // Просматривает свои карты (пагинация)
    @GetMapping("")
    public ResponseEntity<Page<BankCardForUserDto>> getUserCards(
            Pageable pageable,
            BankCardSearchCriteria searchCriteria) {
        return ResponseEntity.ok(bankCardService.getUserCards(pageable, searchCriteria));
    }

    @PreAuthorize("hasAuthority('USER')") // Запрашивает блокировку карты
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<Void> sendBlockRequest(@PathVariable Long cardId) {
        bankCardService.sendBlockRequest(cardId);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAuthority('USER')") // Делает переводы между своими картами
    @PostMapping("/{sourceCardId}/transfer/{targetCardId}")
    public ResponseEntity<Void> makeTransferBetweenOwnCards(
            @PathVariable Long sourceCardId,
            @PathVariable Long targetCardId,
            @RequestParam BigDecimal amount
    ) {
        bankCardService.makeTransfer(sourceCardId, targetCardId, amount);
        return ResponseEntity.ok().build();
    }


//    ✅ Возможности
//    Администратор:
//
//    +Создаёт, блокирует, активирует, удаляет карты
//    Управляет пользователями
//    +Видит все карты
//
//    Пользователь:
//
//    +Просматривает свои карты (+поиск и +пагинация)
//    +Запрашивает блокировку карты
//    +Делает переводы между своими картами
//    Смотрит баланс
}
