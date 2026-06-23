package com.bibliogo.resena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResenaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResenaServiceApplication.class, args);    
        System.out.println("Aplicación de reseñas iniciada correctamente.");
        System.out.println("Accede a la documentación de la API en: http://localhost:8088/swagger-ui.html");
    }
}