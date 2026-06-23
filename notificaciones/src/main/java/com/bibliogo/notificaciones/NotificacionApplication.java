package com.bibliogo.notificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificacionApplication.class, args);
        System.out.println("Aplicación de notificaciones iniciada correctamente.");
        System.out.println("Accede a la documentación de la API en: http://localhost:8086/swagger-ui.html");
    }
}