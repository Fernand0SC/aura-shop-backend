package com.duoc.aurashop.dto;

import com.duoc.aurashop.entity.Rol;

public record UsuarioResponse(Long id, String nombre, String correo, Rol rol) {}