package com.duoc.aurashop.service;

import com.duoc.aurashop.entity.Categoria;
import com.duoc.aurashop.entity.Producto;
import com.duoc.aurashop.repository.CategoriaRepository;
import com.duoc.aurashop.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Producto getById(Long id) {
        return productoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un producto con id " + id));
    }

    public Producto create(Producto producto) {
        producto.setId(null);
        producto.setCategoria(buscarCategoria(producto));
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto datos) {
        Producto existente = getById(id);
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setOferta(datos.getOferta());
        existente.setImg(datos.getImg());
        existente.setDescripcion(datos.getDescripcion());
        existente.setCategoria(buscarCategoria(datos));
        return productoRepository.save(existente);
    }

    public void delete(Long id) {
        productoRepository.delete(getById(id));
    }

    // Regla de negocio: un producto debe pertenecer a una categoría existente
    private Categoria buscarCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar la categoría");
        }
        String id = producto.getCategoria().getId();
        return categoriaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la categoría " + id));
    }
}