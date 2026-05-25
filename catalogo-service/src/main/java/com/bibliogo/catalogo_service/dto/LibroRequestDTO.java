package com.bibliogo.catalogo_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LibroRequestDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @NotBlank(message = "El ISBN no puede estar vacío")
    private String isbn;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private String descripcion;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1800, message = "El año debe ser válido")
    private Integer anioPublicacion;
}