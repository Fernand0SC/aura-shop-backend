package com.duoc.aurashop.repository;

import com.duoc.aurashop.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuarioCorreoOrderByFechaDesc(String correo);
}