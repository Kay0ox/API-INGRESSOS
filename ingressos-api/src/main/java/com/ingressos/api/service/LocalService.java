package com.ingressos.api.service;

import com.ingressos.api.exception.ResourceNotFoundException;
import com.ingressos.api.model.dto.local.LocalDTO;
import com.ingressos.api.model.dto.local.LocalFormDTO;
import com.ingressos.api.model.entity.Local;
import com.ingressos.api.repository.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalService {

    private final LocalRepository localRepository;

    @Transactional
    public LocalDTO cadastrar(LocalFormDTO form) {
        Local local = Local.builder()
                .nome(form.getNome())
                .endereco(form.getEndereco())
                .cidade(form.getCidade())
                .capacidadeMaxima(form.getCapacidadeMaxima())
                .build();
        return LocalDTO.fromEntity(localRepository.save(local));
    }

    @Transactional(readOnly = true)
    public List<LocalDTO> listarTodos() {
        return localRepository.findAll().stream()
                .map(LocalDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocalDTO buscarPorId(Long id) {
        return LocalDTO.fromEntity(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Local buscarEntidadePorId(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado com o id: " + id));
    }
}
