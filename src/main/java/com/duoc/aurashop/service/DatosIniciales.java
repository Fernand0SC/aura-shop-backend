package com.duoc.aurashop.service;

import com.duoc.aurashop.entity.Categoria;
import com.duoc.aurashop.entity.Rol;
import com.duoc.aurashop.entity.Usuario;
import com.duoc.aurashop.repository.CategoriaRepository;
import com.duoc.aurashop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final PasswordEncoder encoder;
    private final String passwordDemo;

    public DatosIniciales(UsuarioRepository usuarios, CategoriaRepository categorias,
                          PasswordEncoder encoder, @Value("${app.seed.password}") String passwordDemo) {
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.encoder = encoder;
        this.passwordDemo = passwordDemo;
    }

    @Override
    public void run(String... args) {
        if (usuarios.count() == 0) {
            crear("Fernando Serrano", "fernando@duoc.cl", Rol.ADMINISTRADOR);
            crear("Editor Productos", "editor@duoc.cl", Rol.EDITOR_PRODUCTOS);
            crear("Soporte Cliente", "soporte@duoc.cl", Rol.SOPORTE);
            crear("Cliente Demo", "cliente@gmail.com", Rol.CLIENTE);
        }
        if (categorias.count() == 0) {
            categoria("hardware", "Hardware", "Componentes internos para armar y mejorar tu PC.");
            categoria("perifericos", "Periféricos", "Mouse, audífonos, cámaras y más para tu setup.");
            categoria("accesorios", "Accesorios", "Complementos para tu espacio de juego.");
        }
    }

    private void crear(String nombre, String correo, Rol rol) {
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setPassword(encoder.encode(passwordDemo));
        u.setRol(rol);
        usuarios.save(u);
    }

    private void categoria(String id, String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        categorias.save(c);
    }
}