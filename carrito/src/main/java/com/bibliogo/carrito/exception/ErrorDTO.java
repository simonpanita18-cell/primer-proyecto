package com.bibliogo.carrito.exception;

import java.time.LocalDateTime;

public class ErrorDTO {
    private int status;
    private String mensaje;
    private LocalDateTime timestamp;

    public ErrorDTO(int status, String mensaje, LocalDateTime timestamp) {
        this.status = status;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }
    public int getStatus() { return status; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getTimestamp() { return timestamp; }
}