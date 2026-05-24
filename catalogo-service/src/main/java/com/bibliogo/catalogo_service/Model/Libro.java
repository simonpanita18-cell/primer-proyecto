package com.bibliogo.catalogo_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    @Column(nullable = false, length = 100)
    private String autor;

    @NotBlank(message = "La categoría no puede estar vacía")
    @Column(nullable = false, length = 100)
    private String categoria;

    @NotBlank(message = "El ISBN no puede estar vacío")
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 20)
    private String disponibilidad;

    @Column(length = 500)
    private String descripcion;

    @Min(value = 1800, message = "El año debe ser válido")
    private Integer anioPublicacion;
}