package com.ingressos.api.model.dto.evento;

import com.ingressos.api.model.dto.local.LocalDTO;
import com.ingressos.api.model.entity.Evento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class EventoDTO {
    private Long id;
    private String nome;
    private LocalDateTime dataEvento;
    private BigDecimal precoIngresso;
    private Integer capacidadeMaxima;
    private LocalDTO local;
    private long ingressosVendidos;
    private long vagasDisponiveis;

    public static EventoDTO fromEntity(Evento evento) {
        long vendidos = evento.getIngressos() != null ? evento.getIngressos().size() : 0;
        int capacidadeEfetiva = Math.min(evento.getCapacidadeMaxima(), evento.getLocal().getCapacidadeMaxima());
        return EventoDTO.builder()
                .id(evento.getId())
                .nome(evento.getNome())
                .dataEvento(evento.getDataEvento())
                .precoIngresso(evento.getPrecoIngresso())
                .capacidadeMaxima(evento.getCapacidadeMaxima())
                .local(LocalDTO.fromEntity(evento.getLocal()))
                .ingressosVendidos(vendidos)
                .vagasDisponiveis(Math.max(0, capacidadeEfetiva - vendidos))
                .build();
    }
}
