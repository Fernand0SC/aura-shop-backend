package com.duoc.aurashop.controller;

import com.duoc.aurashop.entity.Categoria;
import com.duoc.aurashop.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar categorías")
    @GetMapping
    public List<Categoria> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Obtener una categoría por id")
    @GetMapping("/{id}")
    public Categoria getById(@PathVariable String id) {
        return service.getById(id);
    }

    @Operation(summary = "Crear una categoría")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EDITOR_PRODUCTOS')")
    @PostMapping
    public Categoria create(@Valid @RequestBody Categoria categoria) {
        return service.create(categoria);
    }

    @Operation(summary = "Actualizar una categoría")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EDITOR_PRODUCTOS')")
    @PutMapping("/{id}")
    public Categoria update(@PathVariable String id, @Valid @RequestBody Categoria categoria) {
        return service.update(id, categoria);
    }

    @Operation(summary = "Eliminar una categoría")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EDITOR_PRODUCTOS')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}