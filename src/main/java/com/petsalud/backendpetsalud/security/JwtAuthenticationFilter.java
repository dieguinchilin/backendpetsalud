package com.petsalud.backendpetsalud.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String encabezado = request.getHeader("Authorization");

        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezado.substring(7);

        try {
            String nombreUsuario = jwtService.obtenerUsuario(token);

            if (nombreUsuario != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails usuario = usuarioDetailsService
                        .loadUserByUsername(nombreUsuario);

                if (jwtService.validarToken(token, usuario)) {
                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    usuario.getAuthorities());

                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}