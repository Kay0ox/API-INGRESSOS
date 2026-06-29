package com.ingressos.api.controller;

import com.ingressos.api.model.dto.evento.EventoDTO;
import com.ingressos.api.model.dto.evento.EventoFormDTO;
import com.ingressos.api.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Eventos", description = "Cadastro e consulta de eventos culturais")
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @Operation(summary = "Cadastra um novo evento",
            description = "O evento é vinculado a um Local existente (@ManyToOne).")
    public ResponseEntity<EventoDTO> cadastrar(@Valid @RequestBody EventoFormDTO form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.cadastrar(form));
    }

    @GetMapping
    @Operation(summary = "Lista todos os eventos cadastrados")
    public ResponseEntity<List<EventoDTO>> listarTodos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um evento pelo id")
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }
}
