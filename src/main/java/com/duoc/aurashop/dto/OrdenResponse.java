package com.duoc.aurashop.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponse(Long id, String numero, LocalDateTime fecha, String estado,
                            String cliente, String correo, String entrega, String direccion,
                            Integer total, List<ItemResponse> items) {}