package com.bibliogo.pago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PagoResponseDTO {

    private Integer id;
    private Integer prestamoId;
    private Integer usuarioId;
    private BigDecimal monto;
    private String metodo;
    private String estado;
    private String tipo;
    private LocalDateTime fechaPago;
}