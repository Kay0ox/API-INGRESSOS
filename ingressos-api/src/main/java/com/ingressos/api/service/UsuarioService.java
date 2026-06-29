package com.ingressos.api.service;

import com.ingressos.api.exception.BusinessException;
import com.ingressos.api.exception.ResourceNotFoundException;
import com.ingressos.api.model.dto.usuario.UsuarioDTO;
import com.ingressos.api.model.dto.usuario.UsuarioFormDTO;
import com.ingressos.api.model.entity.Endereco;
import com.ingressos.api.model.entity.Role;
import com.ingressos.api.model.entity.Usuario;
import com.ingressos.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO cadastrar(UsuarioFormDTO form) {
        if (usuarioRepository.existsByEmail(form.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail");
        }

        Endereco endereco = Endereco.builder()
                .rua(form.getEndereco().getRua())
                .numero(form.getEndereco().getNumero())
                .cidade(form.getEndereco().getCidade())
                .estado(form.getEndereco().getEstado())
                .cep(form.getEndereco().getCep())
                .build();

        Usuario usuario = Usuario.builder()
                .nome(form.getNome())
                .email(form.getEmail())
                .senha(passwordEncoder.encode(form.getSenha()))
                .role(Role.CLIENTE)
                .endereco(endereco)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
        return UsuarioDTO.fromEntity(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
