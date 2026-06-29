package com.ingressos.api.model.dto.local;

import com.ingressos.api.model.entity.Local;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LocalDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String cidade;
    private Integer capacidadeMaxima;

    public static LocalDTO fromEntity(Local local) {
        return LocalDTO.builder()
                .id(local.getId())
                .nome(local.getNome())
                .endereco(local.getEndereco())
                .cidade(local.getCidade())
                .capacidadeMaxima(local.getCapacidadeMaxima())
                .build();
    }
}
