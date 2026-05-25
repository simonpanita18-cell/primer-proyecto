package com.bibliogo.resena.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResenaUpdateDTO {

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;
}