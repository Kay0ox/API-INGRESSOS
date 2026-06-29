package com.ingressos.api.model.dto.usuario;

import com.ingressos.api.model.dto.endereco.EnderecoDTO;
import com.ingressos.api.model.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;
    private EnderecoDTO endereco;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .endereco(EnderecoDTO.fromEntity(usuario.getEndereco()))
                .build();
    }
}
