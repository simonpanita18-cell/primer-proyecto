package com.bibliogo.reporte.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReporteRequestDTO {

    @NotBlank(message = "El tipo de reporte es obligatorio, no la dejes vacía")
    private String tipo;

    @NotBlank(message = "Los datos del reporte son obligatorios")
    private String datos;

    @NotBlank(message = "El usuario generador es obligatorio")
    private String generadoPor;

    @NotBlank(message = "La url es obligatoria")
    private String url;
}