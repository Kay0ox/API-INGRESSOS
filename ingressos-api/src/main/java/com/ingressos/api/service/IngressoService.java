package com.ingressos.api.service;

import com.ingressos.api.exception.BusinessException;
import com.ingressos.api.model.dto.ingresso.IngressoDTO;
import com.ingressos.api.model.dto.ingresso.IngressoFormDTO;
import com.ingressos.api.model.dto.ingresso.ResumoIngressosDTO;
import com.ingressos.api.model.entity.Evento;
import com.ingressos.api.model.entity.Ingresso;
import com.ingressos.api.model.entity.Usuario;
import com.ingressos.api.repository.IngressoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngressoService {

    /** Limite de ingressos que um mesmo usuário pode comprar para o mesmo evento. */
    private static final int LIMITE_INGRESSOS_POR_USUARIO_POR_EVENTO = 5;

    private final IngressoRepository ingressoRepository;
    private final EventoService eventoService;

    /**
     * Compra (emite) um novo ingresso para o usuário autenticado.
     *
     * Regra de negócio crítica (validação de duplicidade/limite de transações):
     *  1) Não permite emitir ingressos além da capacidade máxima do evento/local
     *     (lotação esgotada).
     *  2) Limita a compra a, no máximo, 5 ingressos por usuário para o mesmo evento.
     */
    @Transactional
    public IngressoDTO comprarIngresso(IngressoFormDTO form, Usuario usuarioLogado) {
        Evento evento = eventoService.buscarEntidadePorId(form.getEventoId());

        validarCapacidadeELimitePorUsuario(evento, usuarioLogado.getId());

        Ingresso ingresso = Ingresso.builder()
                .usuario(usuarioLogado)
                .evento(evento)
                .dataCompra(LocalDateTime.now())
                .valorPago(evento.getPrecoIngresso())
                .build();

        Ingresso salvo = ingressoRepository.save(ingresso);
        return IngressoDTO.fromEntity(salvo);
    }

    private void validarCapacidadeELimitePorUsuario(Evento evento, Long usuarioId) {
        long ingressosVendidos = ingressoRepository.countByEvento_Id(evento.getId());

        // A capacidade respeitada é a mais restritiva entre a capacidade própria
        // do evento e a capacidade máxima do Local onde ele ocorre.
        int capacidadeEfetiva = Math.min(evento.getCapacidadeMaxima(), evento.getLocal().getCapacidadeMaxima());

        if (ingressosVendidos >= capacidadeEfetiva) {
            throw new BusinessException(
                    "Capacidade máxima do local/evento já foi atingida. Não é possível emitir novos ingressos.");
        }

        long ingressosDoUsuarioParaEvento =
                ingressoRepository.countByEvento_IdAndUsuario_Id(evento.getId(), usuarioId);

        if (ingressosDoUsuarioParaEvento >= LIMITE_INGRESSOS_POR_USUARIO_POR_EVENTO) {
            throw new BusinessException(
                    "Limite de " + LIMITE_INGRESSOS_POR_USUARIO_POR_EVENTO
                            + " ingressos por usuário para este evento já foi atingido.");
        }
    }

    public List<IngressoDTO> listarPorUsuario(Long usuarioId) {
        return ingressoRepository.findByUsuario_Id(usuarioId).stream()
                .map(IngressoDTO::fromEntity)
                .toList();
    }

    /**
     * Endpoint de resumo estatístico (GET /ingressos/resumo).
     * Todas as métricas são calculadas EM MEMÓRIA, usando streams do Java
     * sobre a lista completa de ingressos/eventos (sem queries agregadas no banco):
     *  - total de ingressos vendidos;
     *  - receita total arrecadada;
     *  - taxa de ocupação média dos eventos (vendidos / capacidade);
     *  - evento com maior número de vendas.
     */
    public ResumoIngressosDTO obterResumo() {
        List<Ingresso> todosIngressos = ingressoRepository.findAll();

        if (todosIngressos.isEmpty()) {
            return ResumoIngressosDTO.builder()
                    .totalIngressosVendidos(0)
                    .receitaTotal(BigDecimal.ZERO)
                    .taxaOcupacaoMediaPercentual(0.0)
                    .eventoComMaisVendas(null)
                    .build();
        }

        long totalVendidos = todosIngressos.size();

        BigDecimal receitaTotal = todosIngressos.stream()
                .map(Ingresso::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Agrupa os ingressos por evento (em memória) para calcular ocupação e ranking.
        Map<Evento, Long> vendasPorEvento = todosIngressos.stream()
                .collect(Collectors.groupingBy(Ingresso::getEvento, Collectors.counting()));

        double taxaOcupacaoMedia = vendasPorEvento.entrySet().stream()
                .mapToDouble(entry -> {
                    Evento evento = entry.getKey();
                    long vendidos = entry.getValue();
                    int capacidadeEfetiva = Math.min(evento.getCapacidadeMaxima(), evento.getLocal().getCapacidadeMaxima());
                    return capacidadeEfetiva > 0 ? (vendidos * 100.0) / capacidadeEfetiva : 0.0;
                })
                .average()
                .orElse(0.0);

        Map.Entry<Evento, Long> maisVendido = vendasPorEvento.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElseThrow();

        ResumoIngressosDTO.EventoMaisVendidoDTO eventoMaisVendidoDTO = ResumoIngressosDTO.EventoMaisVendidoDTO.builder()
                .eventoId(maisVendido.getKey().getId())
                .nome(maisVendido.getKey().getNome())
                .quantidadeVendida(maisVendido.getValue())
                .build();

        return ResumoIngressosDTO.builder()
                .totalIngressosVendidos(totalVendidos)
                .receitaTotal(receitaTotal)
                .taxaOcupacaoMediaPercentual(Math.round(taxaOcupacaoMedia * 100) / 100.0)
                .eventoComMaisVendas(eventoMaisVendidoDTO)
                .build();
    }
}
