package com.bibliogo.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
//eureka es un directorio de todos los microserivios del sistema, donde el apigatawey consulta este directorio para saber en dondo enviar
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        System.out.println("================================================");
        System.out.println(" Eureka Server corriendo en: http://localhost:8761");
        System.out.println("================================================");
    }
}
