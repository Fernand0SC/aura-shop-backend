package com.duoc.aurashop.controller;

import com.duoc.aurashop.dto.OrdenRequest;
import com.duoc.aurashop.dto.OrdenResponse;
import com.duoc.aurashop.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@Tag(name = "Órdenes")
public class OrdenController {

    private final OrdenService service;

    public OrdenController(OrdenService service) {
        this.service = service;
    }

    @Operation(summary = "Crear una orden (valida y descuenta stock)")
    @PostMapping
    public OrdenResponse crear(Authentication auth, @Valid @RequestBody OrdenRequest req) {
        return service.crear(auth.getName(), req);
    }

    @Operation(summary = "Listar mis órdenes")
    @GetMapping("/mias")
    public List<OrdenResponse> mias(Authentication auth) {
        return service.mias(auth.getName());
    }

    @Operation(summary = "Listar todas las órdenes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SOPORTE')")
    @GetMapping
    public List<OrdenResponse> todas() {
        return service.todas();
    }

    @Operation(summary = "Obtener una orden por id")
    @GetMapping("/{id}")
    public OrdenResponse porId(@PathVariable Long id, Authentication auth) {
        boolean esPersonal = auth.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_ADMINISTRADOR") || a.getAuthority().equals("ROLE_SOPORTE"));
        return service.porId(id, auth.getName(), esPersonal);
    }
}