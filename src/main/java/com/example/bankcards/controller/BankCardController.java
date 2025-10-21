package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardForUserDto;
import com.example.bankcards.dto.BankCardSearchCriteria;
import com.example.bankcards.service.BankCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Контроллер для осуществления действий с банковскими картами")
public class BankCardController {

    private final BankCardService bankCardService;

    @Autowired
    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @Operation(summary = "Создать карту у пользователя с заданным id",
            description = """
                      Создаёт новую банковскую карту для пользователя с заданным id.
                      Карта создаётся со случайным номером, сроком действия 12 месяцев, статусом ACTIVE и балансом 1000.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')") // Создаёт
    @PostMapping("/users/{userId}")
    public ResponseEntity<BankCardDto> createCardByUserId(@Valid @PathVariable Long userId) {
        BankCardDto createdCard = bankCardService.createCardByUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCard);
    }

    @Operation(summary = "Заблокировать карту по её id",
            description = """
                      Устанавливает статус BLOCKED у карты по заданному cardId.
                      Устанавливает поле blockRequested у карты на false (если запрос на блокировку поступал от владельца карты).
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')") // Блокирует
    @PostMapping("/{cardId}/block")
    public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
        bankCardService.blockCard(cardId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Активировать карту по её id",
            description = """
                      Устанавливает статус ACTIVE у карты по заданному cardId.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')") // Активирует
    @PostMapping("/{cardId}/activate")
    public ResponseEntity<Void> activateCard(@PathVariable Long cardId) {
        bankCardService.activateCard(cardId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить карту по её id",
            description = """
                      Удаляет карту по cardId.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')") // Удаляет
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        bankCardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить список всех банковских карт",
            description = """
                      Возвращает список карт всех пользователей.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')") // Видит все карты
    @GetMapping("/all")
    public ResponseEntity<List<BankCardDto>> getAllCards() {
        return ResponseEntity.ok(bankCardService.getAll());
    }

    @Operation(summary = "Получить список всех карт пользователя по его id",
            description = """
                      Возвращает список карт конкретного пользователя по его userId.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')") // Видит все карты пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankCardDto>> getAllUserCards(@PathVariable Long userId) {
        return ResponseEntity.ok(bankCardService.getAllByUserId(userId));
    }

    @Operation(summary = "Получить список всех карт текущего пользователя",
            description = """
                      Возвращает список карт текущего авторизованного пользователя.
                      Возможен поиск по балансу, сроку действия и статусам карт.
                      Доступна пагинация.
                      Доступ: USER
                    """)
    @PreAuthorize("hasAuthority('USER')") // Просматривает свои карты (пагинация+поиск)
    @GetMapping("")
    public ResponseEntity<Page<BankCardForUserDto>> getUserCards(
            Pageable pageable,
            BankCardSearchCriteria searchCriteria) {
        return ResponseEntity.ok(bankCardService.getUserCards(pageable, searchCriteria));
    }

    @Operation(summary = "Запросить блокировку карты по id",
            description = """
                      Устанавливает поле blockRequested у карты текущего пользователя на true.
                      Доступ: USER
                    """)
    @PreAuthorize("hasAuthority('USER')") // Запрашивает блокировку карты
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<Void> sendBlockRequest(@PathVariable Long cardId) {
        bankCardService.sendBlockRequest(cardId);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Осуществить перевод денег между картами пользователя",
            description = """
                      Переводит указанную сумму с одной карты текущего пользователя на другую.
                      Доступ: USER
                    """)
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
//    +Управляет пользователями
//    +Видит все карты
//
//    Пользователь:
//
//    +Просматривает свои карты (+поиск и +пагинация)
//    +Запрашивает блокировку карты
//    +Делает переводы между своими картами
//    Смотрит баланс
}
