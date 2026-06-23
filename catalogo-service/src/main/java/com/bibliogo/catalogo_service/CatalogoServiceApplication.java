package com.bibliogo.catalogo_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoServiceApplication.class, args);
		System.out.println("================================================");
		System.out.println(" Catalogo Service corriendo en: http://localhost:8082");
		System.out.println(" Swagger UI corriendo en: http://localhost:8082/swagger-ui/index.html");
		System.out.println("================================================");
	}

}
