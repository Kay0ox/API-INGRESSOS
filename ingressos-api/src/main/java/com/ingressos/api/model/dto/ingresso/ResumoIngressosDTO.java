package com.ingressos.api.model.dto.ingresso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ResumoIngressosDTO {

    private long totalIngressosVendidos;
    private BigDecimal receitaTotal;
    private double taxaOcupacaoMediaPercentual;
    private EventoMaisVendidoDTO eventoComMaisVendas;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class EventoMaisVendidoDTO {
        private Long eventoId;
        private String nome;
        private long quantidadeVendida;
    }
}
