package com.duoc.aurashop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Categoria {

    @Id
    private String id; // por ejemplo "hardware", "perifericos"

    private String nombre;
    private String descripcion;
}