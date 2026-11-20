package com.duoc.aurashop.security;

import com.duoc.aurashop.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessMs;
    private final long refreshMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.access-ms}") long accessMs,
                      @Value("${jwt.refresh-ms}") long refreshMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMs = accessMs;
        this.refreshMs = refreshMs;
    }

    // Crea un token firmado. refresh=true genera el token de renovación (dura más)
    public String generar(Usuario u, boolean refresh) {
        long duracion = refresh ? refreshMs : accessMs;
        return Jwts.builder()
                .subject(u.getCorreo())
                .claim("rol", u.getRol().name())
                .claim("tipo", refresh ? "refresh" : "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + duracion))
                .signWith(key)
                .compact();
    }

    // Lanza JwtException si el token es inválido, fue alterado o expiró
    public Claims leer(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }
}