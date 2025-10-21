package com.example.bankcards.service;

import com.example.bankcards.dto.response.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.enums.BankCardStatus;
import com.example.bankcards.entity.enums.BankUserRole;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.util.DtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BankCardServiceTests {

    @InjectMocks
    private BankCardService bankCardService;

    @Mock
    private BankCardRepository repository;

    @Mock
    private BankUserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        var card1 = new BankCard("1234-5678-9012-3456", new BankUser(), OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.TEN);
        var card2 = new BankCard("9876-5432-1098-7654", new BankUser(), OffsetDateTime.now(), BankCardStatus.BLOCKED, BigDecimal.ZERO);

        when(repository.findAll()).thenReturn(Arrays.asList(card1, card2));

        List<BankCardDto> result = bankCardService.getAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(DtoConverter.convertBankCardToDto(card1));
        assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(DtoConverter.convertBankCardToDto(card2));
    }

    @Test
    public void testGetAllByUserId() {
        var user = new BankUser(1L, "test_user", "test_password", "Иван", "Иванов", BankUserRole.USER);
        var card1 = new BankCard("1234-5678-9012-3456", user, OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.TEN);
        var card2 = new BankCard("9876-5432-1098-7654", user, OffsetDateTime.now(), BankCardStatus.BLOCKED, BigDecimal.ZERO);

        when(repository.findByUser_Id(1L)).thenReturn(Arrays.asList(card1, card2));

        List<BankCardDto> result = bankCardService.getAllByUserId(1L);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(DtoConverter.convertBankCardToDto(card1));
        assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(DtoConverter.convertBankCardToDto(card2));
    }

    @Test
    public void testCreateCardByUserId() {
        var user = new BankUser(1L, "test_user", "test_password", "Иван", "Иванов", BankUserRole.USER);
        when(userService.checkPresenceAndReturn(1L)).thenReturn(user);

        when(repository.save(any(BankCard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BankCardDto result = bankCardService.createCardByUserId(1L);

        assertThat(result.getNumber().length()).isEqualTo(19); // 16 digits + 3 whitespaces for mask
        assertThat(result.getUser().id()).isEqualTo(1L);
        assertThat(result.getExpirationDate()).isAfter(OffsetDateTime.now());
        assertThat(result.getStatus()).isEqualTo(BankCardStatus.ACTIVE);
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testDeleteCard() {
        var card = new BankCard("1234-5678-9012-3456", new BankUser(), OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(card));

        bankCardService.deleteCard(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    public void testBlockCard() {
        var card = new BankCard("1234-5678-9012-3456", new BankUser(), OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(card));

        bankCardService.blockCard(1L);

        assertThat(card.getStatus()).isEqualTo(BankCardStatus.BLOCKED);
        assertThat(card.getBlockRequested()).isFalse();
    }

    @Test
    public void testActivateCard() {
        var card = new BankCard("1234-5678-9012-3456", new BankUser(), OffsetDateTime.now(), BankCardStatus.BLOCKED, BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(card));

        bankCardService.activateCard(1L);

        assertThat(card.getStatus()).isEqualTo(BankCardStatus.ACTIVE);
    }

    @Test
    public void testSendBlockRequest() {
        var user = new BankUser(1L, "test_user", "test_password", "Иван", "Иванов", BankUserRole.USER);
        var card = new BankCard("1234-5678-9012-3456", user, OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.TEN);
        when(repository.findById(1L)).thenReturn(Optional.of(card));

        Authentication auth = new UsernamePasswordAuthenticationToken("test_user", "");
        SecurityContextHolder.getContext().setAuthentication(auth);

        bankCardService.sendBlockRequest(1L);

        assertThat(card.getBlockRequested()).isTrue();
    }

    @Test
    public void testMakeTransfer() {
        var user = new BankUser(1L, "test_user", "test_password", "Иван", "Иванов", BankUserRole.USER);
        var sourceCard = new BankCard("1234-5678-9012-3456", user, OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.valueOf(1000));
        var targetCard = new BankCard("9876-5432-1098-7654", user, OffsetDateTime.now(), BankCardStatus.ACTIVE, BigDecimal.ZERO);

        when(repository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(repository.findById(2L)).thenReturn(Optional.of(targetCard));

        Authentication auth = new UsernamePasswordAuthenticationToken("test_user", "");
        SecurityContextHolder.getContext().setAuthentication(auth);

        bankCardService.makeTransfer(1L, 2L, BigDecimal.valueOf(500));

        assertThat(sourceCard.getBalance()).isEqualTo(BigDecimal.valueOf(500));
        assertThat(targetCard.getBalance()).isEqualTo(BigDecimal.valueOf(500));
    }
}
