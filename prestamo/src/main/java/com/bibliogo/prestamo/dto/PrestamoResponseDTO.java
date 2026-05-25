package com.bibliogo.prestamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PrestamoResponseDTO {

    private Integer id;
    private Integer usuarioId;
    private Integer libroId;
    private String tituloLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private LocalDate fechaDevolucionReal;
    private String estado;
    private String observaciones;
}