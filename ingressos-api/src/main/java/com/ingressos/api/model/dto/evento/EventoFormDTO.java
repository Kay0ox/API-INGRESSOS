package com.ingressos.api.model.dto.evento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoFormDTO {

    @NotBlank(message = "O nome do evento é obrigatório")
    private String nome;

    @NotNull(message = "A data do evento é obrigatória")
    @Future(message = "A data do evento deve estar no futuro")
    private LocalDateTime dataEvento;

    @NotNull(message = "O preço do ingresso é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private BigDecimal precoIngresso;

    @NotNull(message = "A capacidade máxima do evento é obrigatória")
    @Min(value = 1, message = "A capacidade máxima deve ser maior que zero")
    private Integer capacidadeMaxima;

    @NotNull(message = "O id do local do evento é obrigatório")
    private Long localId;
}
