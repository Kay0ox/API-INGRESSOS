package com.ingressos.api.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de autenticação do usuário")
public class LoginFormDTO {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail em formato inválido")
    @Schema(example = "usuario@email.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(example = "senha123")
    private String senha;
}
