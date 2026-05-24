package com.bibliogo.envio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El id del préstamo es obligatorio")
    @Column(nullable = false)
    private Integer prestamoId;

    @NotNull(message = "El id del usuario es obligatorio")
    @Column(nullable = false)
    private Integer usuarioId;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Column(nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "La comuna no puede estar vacía")
    @Column(nullable = false, length = 100)
    private String comuna;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;
}