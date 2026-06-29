package com.ingressos.api.controller;

import com.ingressos.api.model.dto.local.LocalDTO;
import com.ingressos.api.model.dto.local.LocalFormDTO;
import com.ingressos.api.service.LocalService;
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
@RequestMapping("/locais")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Locais", description = "Cadastro e consulta de locais de eventos (teatros, estádios, etc.)")
public class LocalController {

    private final LocalService localService;

    @PostMapping
    @Operation(summary = "Cadastra um novo local")
    public ResponseEntity<LocalDTO> cadastrar(@Valid @RequestBody LocalFormDTO form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(localService.cadastrar(form));
    }

    @GetMapping
    @Operation(summary = "Lista todos os locais cadastrados")
    public ResponseEntity<List<LocalDTO>> listarTodos() {
        return ResponseEntity.ok(localService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um local pelo id")
    public ResponseEntity<LocalDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localService.buscarPorId(id));
    }
}
