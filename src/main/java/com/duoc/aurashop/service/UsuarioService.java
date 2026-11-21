package com.duoc.aurashop.service;

import com.duoc.aurashop.dto.UsuarioRequest;
import com.duoc.aurashop.dto.UsuarioResponse;
import com.duoc.aurashop.entity.Usuario;
import com.duoc.aurashop.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<UsuarioResponse> getAll() {
        return repository.findAll().stream().map(this::aDto).toList();
    }

    public UsuarioResponse getById(Long id) {
        return aDto(buscar(id));
    }

    public UsuarioResponse create(UsuarioRequest req) {
        if (req.password() == null || req.password().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 4 caracteres");
        }
        if (repository.existsByCorreo(req.correo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setCorreo(req.correo());
        u.setPassword(encoder.encode(req.password()));
        u.setRol(req.rol());
        return aDto(repository.save(u));
    }

    public UsuarioResponse update(Long id, UsuarioRequest req) {
        Usuario u = buscar(id);
        if (!u.getCorreo().equals(req.correo()) && repository.existsByCorreo(req.correo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        u.setNombre(req.nombre());
        u.setCorreo(req.correo());
        u.setRol(req.rol());
        if (req.password() != null && !req.password().isBlank()) {
            u.setPassword(encoder.encode(req.password()));
        }
        return aDto(repository.save(u));
    }

    public void delete(Long id) {
        repository.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un usuario con id " + id));
    }

    private UsuarioResponse aDto(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getCorreo(), u.getRol());
    }
}