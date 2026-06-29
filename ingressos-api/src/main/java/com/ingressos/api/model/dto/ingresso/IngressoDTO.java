package com.ingressos.api.model.dto.ingresso;

import com.ingressos.api.model.entity.Ingresso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class IngressoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long eventoId;
    private String eventoNome;
    private LocalDateTime dataCompra;
    private BigDecimal valorPago;

    public static IngressoDTO fromEntity(Ingresso ingresso) {
        return IngressoDTO.builder()
                .id(ingresso.getId())
                .usuarioId(ingresso.getUsuario().getId())
                .usuarioNome(ingresso.getUsuario().getNome())
                .eventoId(ingresso.getEvento().getId())
                .eventoNome(ingresso.getEvento().getNome())
                .dataCompra(ingresso.getDataCompra())
                .valorPago(ingresso.getValorPago())
                .build();
    }
}
