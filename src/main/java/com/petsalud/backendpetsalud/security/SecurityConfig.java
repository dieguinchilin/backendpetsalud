package com.petsalud.backendpetsalud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            UsuarioDetailsService usuarioDetailsService) {
        this.jwtFilter = jwtFilter;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Registro e inicio de sesión
                .requestMatchers(
                    "/api/usuarios/registro",
                    "/api/login"
                ).permitAll()

                // Administración
                .requestMatchers("/api/admin/**")
                .hasRole("GERENTE")

                // Recursos clínicos
                .requestMatchers("/api/veterinario/**")
                .hasAnyRole("EMPLEADO", "GERENTE")

                // Consultas de mascotas
                .requestMatchers(
                    HttpMethod.GET, "/api/mascotas/**"
                ).hasAnyRole("CLIENTE", "EMPLEADO", "GERENTE")

                // Otras solicitudes requieren autenticación
                .anyRequest().authenticated()
            )
            .userDetailsService(usuarioDetailsService)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}