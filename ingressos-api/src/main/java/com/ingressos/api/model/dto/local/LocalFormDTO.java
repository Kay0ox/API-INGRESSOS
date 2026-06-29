package com.ingressos.api.model.dto.local;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalFormDTO {

    @NotBlank(message = "O nome do local é obrigatório")
    private String nome;

    @NotBlank(message = "O endereço do local é obrigatório")
    private String endereco;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotNull(message = "A capacidade máxima é obrigatória")
    @Min(value = 1, message = "A capacidade máxima deve ser maior que zero")
    private Integer capacidadeMaxima;
}
