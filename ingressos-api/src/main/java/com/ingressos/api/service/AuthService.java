package com.ingressos.api.service;

import com.ingressos.api.model.dto.auth.LoginFormDTO;
import com.ingressos.api.model.dto.auth.LoginResponseDTO;
import com.ingressos.api.model.entity.Usuario;
import com.ingressos.api.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public LoginResponseDTO login(LoginFormDTO form) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail(), form.getSenha())
            );
        } catch (Exception ex) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        Usuario usuario = usuarioService.buscarEntidadePorEmail(form.getEmail());
        String token = tokenService.generateToken(usuario);

        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .expiraEmMs(tokenService.getExpirationMs())
                .build();
    }
}
