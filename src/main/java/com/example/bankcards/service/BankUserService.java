package com.example.bankcards.service;

import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.enums.BankUserRole;
import com.example.bankcards.exception.BankUserNotFoundException;
import com.example.bankcards.repository.BankUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BankUserService {
    private final BankUserRepository repository;



    @Transactional(readOnly = true)
    public BankUser getUserOrThrow(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> BankUserNotFoundException.byId(userId));
    }


    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public BankUser save(BankUser user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public BankUser create(BankUser user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public BankUser getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public BankUser getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(BankUserRole.ADMIN);
        save(user);
    }
}
