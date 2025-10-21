package com.example.bankcards.controller;

import com.example.bankcards.dto.response.BankCardDto;
import com.example.bankcards.dto.response.BankUserDto;
import com.example.bankcards.entity.enums.BankCardStatus;
import com.example.bankcards.entity.enums.BankUserRole;
import com.example.bankcards.service.BankCardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {BankCardController.class})
@WebMvcTest(controllers = BankCardController.class)
@ExtendWith(MockitoExtension.class)
class BankCardControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankCardService bankCardService;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreateCardByUserId() throws Exception {
        BankCardDto expectedCard = new BankCardDto(
                1L,
                "1234-5678-9012-3456",
                new BankUserDto(1L, "John", "Doe", BankUserRole.USER),
                OffsetDateTime.now(),
                BankCardStatus.ACTIVE,
                BigDecimal.valueOf(1000),
                false
        );

        when(bankCardService.createCardByUserId(anyLong())).thenReturn(expectedCard);

        mockMvc.perform(post("/bank_cards/users/{userId}", 1L)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testBlockCard() throws Exception {
        doNothing().when(bankCardService).blockCard(anyLong());

        mockMvc.perform(post("/bank_cards/{cardId}/block", 1L)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testActivateCard() throws Exception {
        doNothing().when(bankCardService).activateCard(anyLong());

        mockMvc.perform(post("/bank_cards/{cardId}/activate", 1L)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testDeleteCard() throws Exception {
        doNothing().when(bankCardService).deleteCard(anyLong());

        mockMvc.perform(delete("/bank_cards/{cardId}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testGetAllCards() throws Exception {
        List<BankCardDto> cards = Arrays.asList(
                new BankCardDto(
                        1L,
                        "1234-5678-9012-3456",
                        new BankUserDto(1L, "John", "Doe", BankUserRole.USER),
                        OffsetDateTime.now(),
                        BankCardStatus.ACTIVE,
                        BigDecimal.valueOf(1000),
                        false
                ),
                new BankCardDto(
                        2L,
                        "9876-5432-1098-7654",
                        new BankUserDto(2L, "Alice", "Smith", BankUserRole.USER),
                        OffsetDateTime.now(),
                        BankCardStatus.BLOCKED,
                        BigDecimal.ZERO,
                        true
                )
        );

        when(bankCardService.getAll()).thenReturn(cards);

        mockMvc.perform(get("/bank_cards/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testGetAllUserCards() throws Exception {
        List<BankCardDto> cards = Arrays.asList(
                new BankCardDto(
                        1L,
                        "1234-5678-9012-3456",
                        new BankUserDto(1L, "John", "Doe", BankUserRole.USER),
                        OffsetDateTime.now(),
                        BankCardStatus.ACTIVE,
                        BigDecimal.valueOf(1000),
                        false
                ),
                new BankCardDto(
                        2L,
                        "9876-5432-1098-7654",
                        new BankUserDto(1L, "John", "Doe", BankUserRole.USER),
                        OffsetDateTime.now(),
                        BankCardStatus.BLOCKED,
                        BigDecimal.ZERO,
                        true
                )
        );

        when(bankCardService.getAllByUserId(anyLong())).thenReturn(cards);

        mockMvc.perform(get("/bank_cards/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testSendBlockRequest() throws Exception {
        doNothing().when(bankCardService).sendBlockRequest(anyLong());

        mockMvc.perform(post("/bank_cards/{cardId}/block-request", 1L)
                        .with(csrf()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void testMakeTransferBetweenOwnCards() throws Exception {
        doNothing().when(bankCardService).makeTransfer(anyLong(), anyLong(), any(BigDecimal.class));

        mockMvc.perform(post("/bank_cards/{sourceCardId}/transfer/{targetCardId}",
                        1L, 2L)
                        .param("amount", "100.00")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

}
