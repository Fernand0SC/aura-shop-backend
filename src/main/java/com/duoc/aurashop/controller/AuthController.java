package com.duoc.aurashop.controller;

import com.duoc.aurashop.dto.*;
import com.duoc.aurashop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Iniciar sesión (devuelve access y refresh token)")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }

    @Operation(summary = "Renovar tokens con el refresh token")
    @PostMapping("/refresh")
    public LoginResponse refresh(@Valid @RequestBody RefreshRequest req) {
        return service.refresh(req.refreshToken());
    }

    @Operation(summary = "Registrar un cliente nuevo")
    @PostMapping("/registro")
    public UsuarioResponse registro(@Valid @RequestBody RegistroRequest req) {
        return service.registrar(req);
    }

    @Operation(summary = "Datos del usuario autenticado")
    @GetMapping("/me")
    public UsuarioResponse me(Authentication auth) {
        return service.me(auth.getName());
    }
}