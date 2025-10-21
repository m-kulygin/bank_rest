package com.example.bankcards.controller;

import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.dto.BankUserUpdateDto;
import com.example.bankcards.service.BankUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank_users")
@Tag(name = "Контроллер для управления пользователями системы")
@Validated
public class BankUserController {

    private final BankUserService bankUserService;

    @Autowired
    public BankUserController(BankUserService bankUserService) {
        this.bankUserService = bankUserService;
    }

    @Operation(summary = "Получить список всех пользователей системы",
            description = """
                      Возвращает информацию о всех пользователях системы.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<BankUserDto>> findAllBankUsers() {
        return ResponseEntity.ok(bankUserService.getAll());
    }

    @Operation(summary = "Удалить пользователя по id",
            description = """
                      Удаляет пользователя с заданным id.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @NotNull(message = "userId не должно быть пустым")
            @Positive(message = "userId должно быть положительным")
            Long userId) {
        bankUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить информацию пользователя по id",
            description = """
                      Обновляет информацию пользователя: можно обновить имя, фамилию и роль в системе.
                      Доступ: ADMIN
                    """)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable
            @NotNull(message = "userId не должно быть пустым")
            @Positive(message = "userId должно быть положительным")
            Long userId,
            @RequestBody
            @Valid
            BankUserUpdateDto userUpdateDto) {
        bankUserService.update(userId, userUpdateDto);
        return ResponseEntity.ok().build();
    }
}
