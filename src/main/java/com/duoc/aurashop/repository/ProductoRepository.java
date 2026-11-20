package com.duoc.aurashop.repository;

import com.duoc.aurashop.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}