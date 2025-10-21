package com.example.bankcards.controller;

import com.example.bankcards.dto.BankUserDto;
import com.example.bankcards.dto.BankUserUpdateDto;
import com.example.bankcards.entity.enums.BankUserRole;
import com.example.bankcards.service.BankUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {BankUserController.class})
@WebMvcTest(controllers = BankUserController.class)
@ExtendWith(MockitoExtension.class)
class BankUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankUserService bankUserService;


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testFindAllBankUsers() throws Exception {

        BankUserDto userDto = new BankUserDto(1L, "John", "Doe", BankUserRole.USER);
        List<BankUserDto> users = List.of(userDto);

        when(bankUserService.getAll()).thenReturn(users);

        mockMvc.perform(get("/bank_users/all")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testDeleteUser() throws Exception {
        doNothing().when(bankUserService).deleteUser(anyLong());

        mockMvc.perform(delete("/bank_users/{userId}", 1L).with(csrf()))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testUpdateUser() throws Exception {
        BankUserUpdateDto dto = new BankUserUpdateDto("Jane", "Smith", BankUserRole.ADMIN);


        doAnswer(invocation -> null).when(bankUserService).update(eq(1L), any(BankUserUpdateDto.class));

        mockMvc.perform(
                        put("/bank_users/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(dto)))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
