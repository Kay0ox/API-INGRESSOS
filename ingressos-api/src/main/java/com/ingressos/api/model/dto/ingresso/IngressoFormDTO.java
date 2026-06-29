package com.ingressos.api.model.dto.ingresso;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngressoFormDTO {

    @NotNull(message = "O id do evento é obrigatório")
    private Long eventoId;
}
