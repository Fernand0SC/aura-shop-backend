package com.duoc.aurashop.service;

import com.duoc.aurashop.entity.Categoria;
import com.duoc.aurashop.entity.Producto;
import com.duoc.aurashop.entity.Rol;
import com.duoc.aurashop.entity.Usuario;
import com.duoc.aurashop.repository.CategoriaRepository;
import com.duoc.aurashop.repository.ProductoRepository;
import com.duoc.aurashop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;
    private final PasswordEncoder encoder;
    private final String passwordDemo;

    public DatosIniciales(UsuarioRepository usuarios, CategoriaRepository categorias,
                          ProductoRepository productos, PasswordEncoder encoder,
                          @Value("${app.seed.password}") String passwordDemo) {
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.productos = productos;
        this.encoder = encoder;
        this.passwordDemo = passwordDemo;
    }

    @Override
    public void run(String... args) {
        if (usuarios.count() == 0) {
            usuario("Fernando Serrano", "fernando@duoc.cl", Rol.ADMINISTRADOR);
            usuario("Editor Productos", "editor@duoc.cl", Rol.EDITOR_PRODUCTOS);
            usuario("Soporte Cliente", "soporte@duoc.cl", Rol.SOPORTE);
            usuario("Cliente Demo", "cliente@gmail.com", Rol.CLIENTE);
        }
        if (categorias.count() == 0) {
            categoria("hardware", "Hardware", "Componentes internos para armar y mejorar tu PC.");
            categoria("perifericos", "Periféricos", "Mouse, audífonos, cámaras y más para tu setup.");
            categoria("accesorios", "Accesorios", "Complementos para tu espacio de juego.");
        }
        if (productos.count() == 0) {
            producto("Tarjeta de Video NVIDIA RTX 4060", 320000, "hardware", "rtx4060.webp", 8, false,
                    "Tarjeta gráfica NVIDIA GeForce RTX 4060 con 8 GB de memoria GDDR6, ideal para jugar en 1080p con trazado de rayos y DLSS 3.");
            producto("Placa Madre B550 AM4", 95000, "hardware", "placaMadreB550.jfif", 12, false,
                    "Placa madre con chipset B550 y socket AM4, compatible con procesadores AMD Ryzen. Soporta memoria DDR4 y almacenamiento NVMe.");
            producto("Fuente de Poder 650W 80+ Bronze", 55000, "hardware", "fuente650w.webp", 15, true,
                    "Fuente de poder de 650 W con certificación 80 PLUS Bronze: eficiente y estable para equipos gamer de gama media.");
            producto("Gabinete ATX Cristal Templado", 45000, "hardware", "gabineteAtx.avif", 10, false,
                    "Gabinete ATX con panel lateral de cristal templado para exhibir tus componentes, con espacio para buena ventilación.");
            producto("Refrigeración Líquida 240mm", 75000, "hardware", "RefrigeracionLíquida.jfif", 4, false,
                    "Kit de refrigeración líquida con radiador de 240 mm para mantener tu procesador a baja temperatura.");
            producto("Memoria RAM 16GB Crucial", 150000, "hardware", "ram16gbCrucial.webp", 20, false,
                    "Memoria RAM de 16 GB marca Crucial para mejorar la multitarea y el rendimiento en juegos.");
            producto("Mouse Gamer", 20000, "perifericos", "mouseGamer.webp", 30, false,
                    "Mouse gamer ergonómico pensado para largas sesiones de juego competitivo.");
            producto("Audífonos Gamer 7.1 Surround", 40000, "perifericos", "Audífonosgamer.jfif", 18, true,
                    "Audífonos gamer con sonido envolvente 7.1 para una experiencia de juego más inmersiva.");
            producto("Mousepad XL Extended (90x40cm)", 12990, "accesorios", "MousepadXL.webp", 25, false,
                    "Mousepad extendido de 90x40 cm que cubre teclado y mouse, con superficie suave para un deslizamiento preciso.");
            producto("Procesador AMD Ryzen 5", 120000, "hardware", "ryzen5.jpg", 9, true,
                    "Procesador AMD Ryzen 5 con excelente relación precio-rendimiento para gaming y productividad.");
            producto("Unidad SSD 1TB NVMe M.2", 100000, "hardware", "ssdnvme1TB.jpg", 3, true,
                    "Unidad de estado sólido de 1 TB con interfaz NVMe M.2 para tiempos de carga muy reducidos.");
            producto("Webcam Streaming 1080p 60fps", 35000, "perifericos", "Webcam.jfif", 11, false,
                    "Webcam Full HD 1080p a 60 cuadros por segundo para streaming y videollamadas.");
        }
    }

    private void usuario(String nombre, String correo, Rol rol) {
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

    private void producto(String nombre, int precio, String categoriaId, String img,
                          int stock, boolean oferta, String descripcion) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCategoria(categorias.findById(categoriaId).orElseThrow());
        p.setImg(img);
        p.setStock(stock);
        p.setOferta(oferta);
        p.setDescripcion(descripcion);
        productos.save(p);
    }
}