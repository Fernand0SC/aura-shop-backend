package com.duoc.aurashop.dto;

import com.duoc.aurashop.entity.Rol;

public record LoginResponse(String accessToken, String refreshToken,
                            String nombre, String correo, Rol rol) {}