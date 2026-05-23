package com.bibliogo.catalogo_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "libros")
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

    public Libro() {}

    public Libro(Integer id, String titulo, String autor, String categoria,
                 String isbn, Integer stock, String disponibilidad,
                 String descripcion, Integer anioPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.stock = stock;
        this.disponibilidad = disponibilidad;
        this.descripcion = descripcion;
        this.anioPublicacion = anioPublicacion;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(Integer anioPublicacion) { this.anioPublicacion = anioPublicacion; }
}