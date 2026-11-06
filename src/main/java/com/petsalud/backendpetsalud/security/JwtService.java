package com.petsalud.backendpetsalud.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey clave;
    private static final long DURACION = 3600000;

    public JwtService(@Value("${jwt.secret}") String secreto) {
        byte[] bytes = Base64.getDecoder().decode(secreto);
        this.clave = Keys.hmacShaKeyFor(bytes);
    }

    public String generarToken(UserDetails usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + DURACION))
                .signWith(clave)
                .compact();
    }

    public String obtenerUsuario(String token) {
        return obtenerClaims(token).getSubject();
    }

    public boolean validarToken(String token, UserDetails usuario) {
        try {
            return obtenerUsuario(token).equals(usuario.getUsername())
                    && obtenerClaims(token)
                            .getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}