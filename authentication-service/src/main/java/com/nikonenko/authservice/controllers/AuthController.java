package com.nikonenko.authservice.controllers;

import com.nikonenko.authservice.dto.AuthRequest;
import com.nikonenko.authservice.dto.AuthResponse;
import com.nikonenko.authservice.dto.RefreshRequest;
import com.nikonenko.authservice.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.CredentialExpiredException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@RestControllerAdvice
@Tag(name="Authentication Controller", description="Responsible for authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register",
            description = "Registers user and returns Access and Refresh token"
    )
    public AuthResponse register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

    @PostMapping(value = "/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Refresh Token",
            description = "Check refresh token and returns new Access and Refresh token"
    )
    public AuthResponse refreshToken(@RequestBody RefreshRequest refreshRequest) throws CredentialExpiredException {
        return authService.refreshTokens(refreshRequest);
    }
}
