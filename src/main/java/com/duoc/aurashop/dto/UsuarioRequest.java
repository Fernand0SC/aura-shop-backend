package com.duoc.aurashop.dto;

import com.duoc.aurashop.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @NotBlank String nombre,
        @NotBlank @Email String correo,
        String password, // obligatoria al crear; opcional al editar
        @NotNull Rol rol) {}