package com.bibliogo.reporte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Column(nullable = false, length = 100)
    private String tipo;

    @NotBlank(message = "Los datos del reporte son obligatorios")
    @Column(nullable = false, length = 1000)
    private String datos;

    @NotBlank(message = "El usuario generador es obligatorio")
    @Column(nullable = false, length = 100)
    private String generadoPor;

    @Column(nullable = false)
    private LocalDateTime generadoEn;
}