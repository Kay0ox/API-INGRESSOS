package com.ingressos.api.service;

import com.ingressos.api.exception.ResourceNotFoundException;
import com.ingressos.api.model.dto.evento.EventoDTO;
import com.ingressos.api.model.dto.evento.EventoFormDTO;
import com.ingressos.api.model.entity.Evento;
import com.ingressos.api.model.entity.Local;
import com.ingressos.api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final LocalService localService;

    @Transactional
    public EventoDTO cadastrar(EventoFormDTO form) {
        Local local = localService.buscarEntidadePorId(form.getLocalId());

        Evento evento = Evento.builder()
                .nome(form.getNome())
                .dataEvento(form.getDataEvento())
                .precoIngresso(form.getPrecoIngresso())
                .capacidadeMaxima(form.getCapacidadeMaxima())
                .local(local)
                .build();

        return EventoDTO.fromEntity(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> listarTodos() {
        return eventoRepository.findAll().stream()
                .map(EventoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventoDTO buscarPorId(Long id) {
        return EventoDTO.fromEntity(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Evento buscarEntidadePorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o id: " + id));
    }
}
