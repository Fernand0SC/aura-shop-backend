package com.duoc.aurashop.service;

import com.duoc.aurashop.dto.*;
import com.duoc.aurashop.entity.ItemOrden;
import com.duoc.aurashop.entity.Orden;
import com.duoc.aurashop.entity.Producto;
import com.duoc.aurashop.entity.Usuario;
import com.duoc.aurashop.repository.OrdenRepository;
import com.duoc.aurashop.repository.ProductoRepository;
import com.duoc.aurashop.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenes;
    private final ProductoRepository productos;
    private final UsuarioRepository usuarios;

    public OrdenService(OrdenRepository ordenes, ProductoRepository productos, UsuarioRepository usuarios) {
        this.ordenes = ordenes;
        this.productos = productos;
        this.usuarios = usuarios;
    }

    // Regla de negocio: valida stock, lo descuenta y calcula el total en el servidor.
    // Si algo falla, @Transactional deshace todo y el stock no queda descontado a medias.
    @Transactional
    public OrdenResponse crear(String correo, OrdenRequest req) {
        Usuario usuario = usuarios.findByCorreo(correo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("Pagada");
        orden.setEntrega(req.entrega());
        orden.setDireccion(req.direccion());

        int total = 0;
        for (ItemRequest it : req.items()) {
            Producto p = productos.findById(it.productoId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No existe el producto " + it.productoId()));
            if (p.getStock() < it.cantidad()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para " + p.getNombre() + " (disponible: " + p.getStock() + ")");
            }
            p.setStock(p.getStock() - it.cantidad());
            productos.save(p);

            ItemOrden item = new ItemOrden();
            item.setOrden(orden);
            item.setProducto(p);
            item.setNombre(p.getNombre());
            item.setPrecio(p.getPrecio());
            item.setCantidad(it.cantidad());
            orden.getItems().add(item);

            total += p.getPrecio() * it.cantidad();
        }
        orden.setTotal(total);
        return aDto(ordenes.save(orden));
    }

    @Transactional(readOnly = true)
    public List<OrdenResponse> mias(String correo) {
        return ordenes.findByUsuarioCorreoOrderByFechaDesc(correo).stream().map(this::aDto).toList();
    }

    @Transactional(readOnly = true)
    public List<OrdenResponse> todas() {
        return ordenes.findAll().stream().map(this::aDto).toList();
    }

    @Transactional(readOnly = true)
    public OrdenResponse porId(Long id, String correo, boolean esPersonal) {
        Orden o = ordenes.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la orden " + id));
        // Un cliente solo puede ver sus propias órdenes
        if (!esPersonal && !o.getUsuario().getCorreo().equals(correo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes ver esta orden");
        }
        return aDto(o);
    }

    private OrdenResponse aDto(Orden o) {
        List<ItemResponse> items = o.getItems().stream()
                .map(i -> new ItemResponse(i.getProducto().getId(), i.getNombre(), i.getPrecio(), i.getCantidad()))
                .toList();
        return new OrdenResponse(o.getId(), String.format("ORD-%05d", o.getId()), o.getFecha(),
                o.getEstado(), o.getUsuario().getNombre(), o.getUsuario().getCorreo(),
                o.getEntrega(), o.getDireccion(), o.getTotal(), items);
    }
}