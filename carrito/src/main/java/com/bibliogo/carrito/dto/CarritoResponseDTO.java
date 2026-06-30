package com.bibliogo.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarritoResponseDTO {

    private Integer id;
    private Integer usuarioId;
    private Integer libroId;
    private String tituloLibro;
    private Integer cantidad;
    private String estado;
}