package com.duoc.aurashop.controller;

import com.duoc.aurashop.dto.UsuarioRequest;
import com.duoc.aurashop.dto.UsuarioResponse;
import com.duoc.aurashop.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar usuarios")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SOPORTE')")
    @GetMapping
    public List<UsuarioResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Obtener un usuario por id")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SOPORTE')")
    @GetMapping("/{id}")
    public UsuarioResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Crear un usuario con el rol indicado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public UsuarioResponse create(@Valid @RequestBody UsuarioRequest req) {
        return service.create(req);
    }

    @Operation(summary = "Actualizar un usuario")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public UsuarioResponse update(@PathVariable Long id, @Valid @RequestBody UsuarioRequest req) {
        return service.update(id, req);
    }

    @Operation(summary = "Eliminar un usuario")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}