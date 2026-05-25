package com.bibliogo.prestamo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PrestamoRequestDTO {

    @NotNull(message = "El id de usuario es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El id del libro es obligatorio")
    private Integer libroId;

    @NotBlank(message = "El título del libro es obligatorio")
    private String tituloLibro;

    private String observaciones;
}