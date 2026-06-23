package com.bibliogo.carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarritoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarritoApplication.class, args);
		System.out.println("CarritoApplication is running...");
		System.out.println("Access the API documentation at: http://localhost:8083/swagger-ui.html");	
	}

}
