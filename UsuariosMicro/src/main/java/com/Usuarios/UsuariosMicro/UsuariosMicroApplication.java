package com.Usuarios.UsuariosMicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosMicroApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosMicroApplication.class, args);
		System.out.println("UsuariosMicroApplication is running...");
		System.out.println("http://localhost:8081/swagger-ui.html");
	}

}
