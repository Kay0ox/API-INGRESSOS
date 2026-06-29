package com.ingressos.api.model.dto.endereco;

import com.ingressos.api.model.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EnderecoDTO {
    private Long id;
    private String rua;
    private String numero;
    private String cidade;
    private String estado;
    private String cep;

    public static EnderecoDTO fromEntity(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }
}
