package com.example.bankcards.controller;

import com.example.bankcards.dto.security.JwtAuthenticationResponse;
import com.example.bankcards.dto.security.SignInRequest;
import com.example.bankcards.dto.security.SignUpRequest;
import com.example.bankcards.service.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Контроллер для регистрации/авторизации пользователей")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация нового пользователя",
            description = """
                      Заводит в системе нового пользователя с ролью USER, заданными username и password.
                      Возвращает jwt регистрации.
                      Доступ: свободный
                    """)
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(
            @RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя",
            description = """
                      Авторизует существующего пользователя по заданным username и password.
                      Возвращает jwt для авторизации.
                      Доступ: свободный
                    """)
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(
            @RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
