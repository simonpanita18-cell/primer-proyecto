package com.bibliogo.envio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnvioApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnvioApplication.class, args);
        System.out.println("EnvioApplication is running...");
        System.out.println("Access the API documentation at: http://localhost:8087/swagger-ui/index.html");
    }
}