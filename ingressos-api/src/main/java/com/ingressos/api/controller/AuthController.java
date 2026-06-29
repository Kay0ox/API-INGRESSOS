package com.ingressos.api.controller;

import com.ingressos.api.model.dto.auth.LoginFormDTO;
import com.ingressos.api.model.dto.auth.LoginResponseDTO;
import com.ingressos.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autentica o usuário e retorna um token JWT", description = "Rota PÚBLICA.")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginFormDTO form) {
        return ResponseEntity.ok(authService.login(form));
    }
}
