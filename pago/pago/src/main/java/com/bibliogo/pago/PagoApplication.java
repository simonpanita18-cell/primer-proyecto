package com.bibliogo.pago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PagoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagoApplication.class, args);
        System.out.println("Aplicación de pagos iniciada correctamente.");
        System.out.println("Accede a la documentación de la API en: http://localhost:8085/swagger-ui.html");
    }
}