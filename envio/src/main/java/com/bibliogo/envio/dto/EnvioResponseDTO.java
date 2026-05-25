package com.bibliogo.envio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnvioResponseDTO {

    private Integer id;
    private Integer prestamoId;
    private Integer usuarioId;
    private String direccion;
    private String comuna;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
}