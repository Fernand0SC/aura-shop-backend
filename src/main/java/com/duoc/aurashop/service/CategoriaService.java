package com.duoc.aurashop.service;

import com.duoc.aurashop.entity.Categoria;
import com.duoc.aurashop.repository.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> getAll() {
        return repository.findAll();
    }

    public Categoria getById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la categoría " + id));
    }

    public Categoria create(Categoria categoria) {
        if (categoria.getId() == null || categoria.getId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar el id de la categoría");
        }
        if (repository.existsById(categoria.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe la categoría " + categoria.getId());
        }
        return repository.save(categoria);
    }

    public Categoria update(String id, Categoria datos) {
        Categoria existente = getById(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return repository.save(existente);
    }

    public void delete(String id) {
        repository.delete(getById(id));
    }
}