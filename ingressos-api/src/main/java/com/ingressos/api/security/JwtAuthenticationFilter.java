package com.ingressos.api.security;

import com.ingressos.api.model.entity.Usuario;
import com.ingressos.api.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filtro executado em toda requisição: extrai o header
 * "Authorization: Bearer <TOKEN>", valida o JWT e, se válido,
 * autentica o usuário no contexto de segurança do Spring.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String email = tokenService.validarTokenEObterEmail(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
                    usuarioOpt.ifPresent(usuario -> {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                }
            } catch (Exception ex) {
                // Token inválido/expirado: limpa o contexto e segue;
                // a rota protegida retornará 401 ao exigir autenticação.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
