package com.bibliogo.prestamo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El id de usuario es obligatorio")
    @Column(nullable = false)
    private Integer usuarioId;

    @NotNull(message = "El id del libro es obligatorio")
    @Column(nullable = false)
    private Integer libroId;

    @NotBlank(message = "El título del libro es obligatorio")
    @Column(nullable = false, length = 200)
    private String tituloLibro;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    @Column(nullable = false)
    private LocalDate fechaDevolucion;

    private LocalDate fechaDevolucionReal;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(length = 200)
    private String observaciones;
}