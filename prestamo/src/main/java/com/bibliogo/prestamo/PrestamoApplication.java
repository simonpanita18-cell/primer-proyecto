package com.bibliogo.prestamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrestamoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrestamoApplication.class, args);
        System.out.println("PrestamoApplication is running...");
        System.out.println("Access the API documentation at: http://localhost:8084/swagger-ui.html");
    }
}