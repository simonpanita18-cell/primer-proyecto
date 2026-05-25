package com.bibliogo.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificacionResponseDTO {

    private Integer id;
    private Integer usuarioId;
    private String tipo;
    private String mensaje;
    private String estado;
    private LocalDateTime creadoEn;
}