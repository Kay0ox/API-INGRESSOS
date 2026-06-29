package com.ingressos.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Evento cultural que ocorre em um Local, com data, capacidade
 * máxima própria e preço do ingresso.
 *
 * Relacionamentos:
 *  - ManyToOne com Local (vários eventos podem ocorrer no mesmo local).
 *  - OneToMany com Ingresso (um evento possui vários ingressos vendidos).
 */
@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "data_evento", nullable = false)
    private LocalDateTime dataEvento;

    @Column(name = "preco_ingresso", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoIngresso;

    /**
     * Capacidade máxima de ingressos para este evento específico.
     * A capacidade efetivamente respeitada na compra é o menor valor
     * entre esta capacidade e a capacidade máxima do Local (ver IngressoService).
     */
    @Column(name = "capacidade_maxima", nullable = false)
    private Integer capacidadeMaxima;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Ingresso> ingressos = new ArrayList<>();
}
