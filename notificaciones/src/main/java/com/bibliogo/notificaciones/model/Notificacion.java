package com.bibliogo.notificacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El id del usuario es obligatorio")
    @Column(nullable = false)
    private Integer usuarioId;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Column(nullable = false, length = 50)
    private String tipo;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime creadoEn;
}