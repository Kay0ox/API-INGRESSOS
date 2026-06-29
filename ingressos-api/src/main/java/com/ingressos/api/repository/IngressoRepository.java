package com.ingressos.api.repository;

import com.ingressos.api.model.entity.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    long countByEvento_Id(Long eventoId);

    long countByEvento_IdAndUsuario_Id(Long eventoId, Long usuarioId);

    List<Ingresso> findByUsuario_Id(Long usuarioId);
}
