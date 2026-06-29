package com.ingressos.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestão de Ingressos - Eventos Culturais",
                version = "1.0.0",
                description = "API para venda de ingressos de eventos culturais, com controle de "
                        + "lotação máxima por local/evento, limite de compra por usuário e "
                        + "estatísticas de vendas calculadas em memória.",
                contact = @Contact(name = "Equipe Ingressos API")
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Informe o token JWT obtido em POST /auth/login, no formato: Bearer <token>"
)
public class OpenApiConfig {
}
