package com.bibliogo.resena.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI resenaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Reseñas - BiblioGo")
                        .description("API REST para la gestión de reseñas y calificaciones de libros del sistema BiblioGo")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BiblioGo Team")
                                .email("soporte@bibliogo.com")
                        )
                );
    }
}