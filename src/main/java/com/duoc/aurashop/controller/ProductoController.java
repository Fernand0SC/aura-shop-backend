package com.duoc.aurashop.controller;

import com.duoc.aurashop.entity.Producto;
import com.duoc.aurashop.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar productos")
    @GetMapping
    public List<Producto> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Obtener un producto por id")
    @GetMapping("/{id}")
    public Producto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Crear un producto")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EDITOR_PRODUCTOS')")
    @PostMapping
    public Producto create(@Valid @RequestBody Producto producto) {
        return service.create(producto);
    }

    @Operation(summary = "Actualizar un producto")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EDITOR_PRODUCTOS')")
    @PutMapping("/{id}")
    public Producto update(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return service.update(id, producto);
    }

    @Operation(summary = "Eliminar un producto")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EDITOR_PRODUCTOS')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}