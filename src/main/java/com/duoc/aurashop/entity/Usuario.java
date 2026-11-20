package com.duoc.aurashop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @JsonIgnore
    @Column(nullable = false)
    private String password; // se guarda con hash BCrypt, nunca en texto plano

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
}