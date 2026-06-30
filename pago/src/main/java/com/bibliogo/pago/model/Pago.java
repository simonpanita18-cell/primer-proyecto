package com.bibliogo.pago.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El id del préstamo es obligatorio")
    @Column(nullable = false)
    private Integer prestamoId;

    @NotNull(message = "El id del usuario es obligatorio")
    @Column(nullable = false)
    private Integer usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Column(nullable = false, length = 50)
    private String metodo;

    @Column(nullable = false, length = 20)
    private String estado;

    @NotBlank(message = "El tipo de pago es obligatorio")
    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false)
    private LocalDateTime fechaPago;
}