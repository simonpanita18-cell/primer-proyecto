package com.bibliogo.resena.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResenaResponseDTO {

    private Integer id;
    private Integer usuarioId;
    private Integer libroId;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime creadoEn;
}