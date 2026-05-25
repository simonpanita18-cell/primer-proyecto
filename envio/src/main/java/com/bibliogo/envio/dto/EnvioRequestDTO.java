package com.bibliogo.envio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EnvioRequestDTO {

    @NotNull(message = "El id del préstamo es obligatorio")
    private Integer prestamoId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Integer usuarioId;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotBlank(message = "La comuna no puede estar vacía")
    private String comuna;
}