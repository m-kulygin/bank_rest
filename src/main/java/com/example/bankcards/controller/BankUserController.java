package com.example.bankcards.controller;

import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.dto.BankUserUpdateDto;
import com.example.bankcards.service.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank_users")
public class BankUserController {

    private final BankUserService bankUserService;

    @Autowired
    public BankUserController(BankUserService bankUserService) {
        this.bankUserService = bankUserService;
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Смотрит всех юзеров
    @GetMapping("/all")
    public ResponseEntity<List<BankUserDto>> findAllBankUsers() {
        return ResponseEntity.ok(bankUserService.getAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')") // Удаляет
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        bankUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")  // Обновляет
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody BankUserUpdateDto userUpdateDto) {
        bankUserService.update(userId, userUpdateDto);
        return ResponseEntity.ok().build();
    }


}
