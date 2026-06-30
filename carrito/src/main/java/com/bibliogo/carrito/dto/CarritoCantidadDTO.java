package com.bibliogo.carrito.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarritoCantidadDTO {

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}