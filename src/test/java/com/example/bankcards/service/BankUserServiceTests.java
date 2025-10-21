package com.example.bankcards.service;

import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.dto.BankUserUpdateDto;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.enums.BankUserRole;
import com.example.bankcards.exception.BankUserLoginAlreadyExistsException;
import com.example.bankcards.exception.BankUserNotFoundException;
import com.example.bankcards.repository.BankUserRepository;
import com.example.bankcards.util.DtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BankUserServiceTests {

    @InjectMocks
    private BankUserService service;

    @Mock
    private BankUserRepository repository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        var user1 = new BankUser(1L, "test1", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Иван", "Иванов", BankUserRole.USER);
        var user2 = new BankUser(2L, "test2", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Анна", "Петрова", BankUserRole.USER);

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        List<BankUserDto> result = service.getAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).usingRecursiveComparison().ignoringFields("role").isEqualTo(DtoConverter.convertBankUserToDto(user1));
        assertThat(result.get(1)).usingRecursiveComparison().ignoringFields("role").isEqualTo(DtoConverter.convertBankUserToDto(user2));
    }

    @Test
    public void testCreate() {
        var newUser = new BankUser(null, "new_user", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Новый", "Пользователь", BankUserRole.USER);
        var savedUser = new BankUser(3L, "new_user", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Новый", "Пользователь", BankUserRole.USER);

        when(repository.existsByUsername("new_user")).thenReturn(false);
        when(repository.save(newUser)).thenReturn(savedUser);

        BankUser result = service.create(newUser);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getUsername()).isEqualTo("new_user");
    }

    @Test
    public void testUpdate() {
        var existingUser = new BankUser(1L, "old_user", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Иван", "Иванов", BankUserRole.USER);
        var updatedDto = new BankUserUpdateDto("Андрей", "Андреев", BankUserRole.ADMIN);

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(any(BankUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BankUser result = service.update(1L, updatedDto);

        assertThat(result.getFirstName()).isEqualTo("Андрей");
        assertThat(result.getLastName()).isEqualTo("Андреев");
        assertThat(result.getRole()).isEqualTo(BankUserRole.ADMIN);
    }

    @Test
    public void testDeleteUser() {
        var userToDelete = new BankUser(1L, "delete_me", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Удаленный", "Пользователь", BankUserRole.USER);

        when(repository.findById(1L)).thenReturn(Optional.of(userToDelete));

        service.deleteUser(1L);

        verify(repository).delete(userToDelete);
    }

    @Test
    public void testGetByUsername() {
        var user = new BankUser(1L, "test_user", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Тестовый", "Пользователь", BankUserRole.USER);

        when(repository.findByUsername("test_user")).thenReturn(Optional.of(user));

        BankUser result = service.getByUsername("test_user");

        assertThat(result.getUsername()).isEqualTo("test_user");
    }

    @Test
    public void testCheckPresenceAndReturnThrowsWhenUserNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.checkPresenceAndReturn(1L))
                .isInstanceOf(BankUserNotFoundException.class)
                .hasMessageContaining("Could not find bank user with id '1'");
    }

    @Test
    public void testCreateThrowsIfUsernameAlreadyExists() {
        var duplicateUser = new BankUser(null, "duplicate", "$2a$10$vYcZQKzYlS7oB4ywEiXpP.XeVbCQJkRzNxw3YdyPKVJK5dXKwqN3.", "Дубликат", "Пользователь", BankUserRole.USER);

        when(repository.existsByUsername("duplicate")).thenReturn(true);

        assertThatThrownBy(() -> service.create(duplicateUser))
                .isInstanceOf(BankUserLoginAlreadyExistsException.class)
                .hasMessageContaining("Bank user already exists with login 'duplicate'");
    }
}
