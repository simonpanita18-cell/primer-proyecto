package com.bibliogo.carrito.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

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

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 20)
    private String estado;
}