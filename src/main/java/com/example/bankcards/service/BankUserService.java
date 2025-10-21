package com.example.bankcards.service;

import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.dto.BankUserUpdateDto;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.exception.BankUserLoginAlreadyExistsException;
import com.example.bankcards.exception.BankUserNotFoundException;
import com.example.bankcards.repository.BankUserRepository;
import com.example.bankcards.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankUserService {
    private final BankUserRepository bankUserRepository;

    public BankUser checkPresenceAndReturn(Long userId) {
        Optional<BankUser> bankUser = bankUserRepository.findById(userId);
        if (bankUser.isEmpty()) {
            throw BankUserNotFoundException.byId(userId);
        }
        return bankUser.get();
    }

    @Transactional(readOnly = true)
    public List<BankUserDto> getAll() {
        List<BankUser> users = bankUserRepository.findAll();
        return users.stream()
                .map(DtoConverter::convertBankUserToDto)
                .toList();
    }

    @Transactional
    public void deleteUser(Long userId) {
        BankUser user = checkPresenceAndReturn(userId);
        bankUserRepository.delete(user);
    }

    @Transactional
    public BankUser create(BankUser user) {
        if (bankUserRepository.existsByUsername(user.getUsername())) {
            throw BankUserLoginAlreadyExistsException.byLogin(user.getUsername());
        }
        return bankUserRepository.save(user);
    }

    @Transactional
    public BankUser update(Long userId, BankUserUpdateDto updateDto) {
        BankUser user = checkPresenceAndReturn(userId);
        user.setFirstName(updateDto.firstName());
        user.setLastName(updateDto.lastName());
        user.setRole(updateDto.role());
        return bankUserRepository.save(user);
    }

    public BankUser getByUsername(String username) {
        return bankUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
