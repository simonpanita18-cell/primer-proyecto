package com.bibliogo.pago.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El id del préstamo es obligatorio")
    private Integer prestamoId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodo;

    @NotBlank(message = "El tipo de pago es obligatorio")
    private String tipo;
}