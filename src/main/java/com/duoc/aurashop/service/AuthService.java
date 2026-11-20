package com.duoc.aurashop.service;

import com.duoc.aurashop.dto.*;
import com.duoc.aurashop.entity.Rol;
import com.duoc.aurashop.entity.Usuario;
import com.duoc.aurashop.repository.UsuarioRepository;
import com.duoc.aurashop.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository repository, PasswordEncoder encoder, JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest req) {
        // Mismo mensaje si falla el correo o la contraseña, para no revelar cuál fue
        Usuario u = repository.findByCorreo(req.correo())
                .filter(x -> encoder.matches(req.password(), x.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Correo o contraseña incorrectos"));
        return respuesta(u);
    }

    public LoginResponse refresh(String refreshToken) {
        try {
            Claims claims = jwtService.leer(refreshToken);
            if (!"refresh".equals(claims.get("tipo", String.class))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
            }
            Usuario u = repository.findByCorreo(claims.getSubject())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
            return respuesta(u);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
        }
    }

    public UsuarioResponse registrar(RegistroRequest req) {
        if (repository.existsByCorreo(req.correo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setCorreo(req.correo());
        u.setPassword(encoder.encode(req.password()));
        u.setRol(Rol.CLIENTE); // el rol lo fija el servidor, nunca viene del cliente
        return aDto(repository.save(u));
    }

    public UsuarioResponse me(String correo) {
        return aDto(repository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado")));
    }

    private LoginResponse respuesta(Usuario u) {
        return new LoginResponse(jwtService.generar(u, false), jwtService.generar(u, true),
                u.getNombre(), u.getCorreo(), u.getRol());
    }

    private UsuarioResponse aDto(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getCorreo(), u.getRol());
    }
}