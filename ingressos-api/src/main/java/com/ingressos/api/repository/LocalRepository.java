package com.ingressos.api.repository;

import com.ingressos.api.model.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
}
