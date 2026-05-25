package com.bibliogo.catalogo_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroResponseDTO {

    private Integer id;
    private String titulo;
    private String autor;
    private String categoria;
    private String isbn;
    private Integer stock;
    private String disponibilidad;
    private String descripcion;
    private Integer anioPublicacion;
}