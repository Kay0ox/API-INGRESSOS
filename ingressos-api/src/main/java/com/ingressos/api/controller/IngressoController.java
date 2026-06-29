package com.ingressos.api.controller;

import com.ingressos.api.model.dto.ingresso.IngressoDTO;
import com.ingressos.api.model.dto.ingresso.IngressoFormDTO;
import com.ingressos.api.model.dto.ingresso.ResumoIngressosDTO;
import com.ingressos.api.model.entity.Usuario;
import com.ingressos.api.service.IngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ingressos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ingressos", description = "Compra de ingressos e estatísticas de vendas")
public class IngressoController {

    private final IngressoService ingressoService;

    @PostMapping
    @Operation(summary = "Compra um ingresso para o evento informado",
            description = "Valida a capacidade máxima do evento/local e o limite de 5 "
                    + "ingressos por usuário para o mesmo evento.")
    public ResponseEntity<IngressoDTO> comprar(@Valid @RequestBody IngressoFormDTO form,
                                                @AuthenticationPrincipal Usuario usuarioLogado) {
        IngressoDTO ingressoDTO = ingressoService.comprarIngresso(form, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingressoDTO);
    }

    @GetMapping("/meus")
    @Operation(summary = "Lista os ingressos comprados pelo usuário autenticado")
    public ResponseEntity<List<IngressoDTO>> listarMeusIngressos(@AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(ingressoService.listarPorUsuario(usuarioLogado.getId()));
    }

    @GetMapping("/resumo")
    @Operation(summary = "Retorna estatísticas de vendas calculadas em memória",
            description = "Total de ingressos vendidos, receita total, taxa de ocupação "
                    + "média dos eventos e o evento com maior número de vendas.")
    public ResponseEntity<ResumoIngressosDTO> obterResumo() {
        return ResponseEntity.ok(ingressoService.obterResumo());
    }
}
