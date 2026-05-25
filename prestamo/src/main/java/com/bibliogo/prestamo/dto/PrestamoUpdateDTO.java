package com.bibliogo.prestamo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PrestamoUpdateDTO {

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    private String observaciones;

    public Integer getLibroId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLibroId'");
    }
}