package com.bibliogo.carrito.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarritoRequestDTO {

    @NotNull(message = "El id de usuario es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El id del libro es obligatorio")
    private Integer libroId;

    @NotBlank(message = "El título del libro es obligatorio")
    private String tituloLibro;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}