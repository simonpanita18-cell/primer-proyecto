package com.bibliogo.notificaciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NotificacionRequestDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Integer usuarioId;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;
}