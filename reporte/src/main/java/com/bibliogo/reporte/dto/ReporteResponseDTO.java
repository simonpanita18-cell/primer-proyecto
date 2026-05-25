package com.bibliogo.reporte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReporteResponseDTO {

    private Integer id;
    private String tipo;
    private String datos;
    private String generadoPor;
    private LocalDateTime generadoEn;
}