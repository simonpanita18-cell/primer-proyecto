package com.bibliogo.reporte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReporteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReporteApplication.class, args);
        System.out.println("Aplicación de reportes iniciada correctamente.");
        System.out.println("Accede a la documentación de la API en: http://localhost:8089/swagger-ui.html");
    }
}