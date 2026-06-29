package com.ingressos.api.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "Resposta de login contendo o token JWT")
public class LoginResponseDTO {
    private String token;
    private String tipo;
    private Long expiraEmMs;
}
