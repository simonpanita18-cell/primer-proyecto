package com.bibliogo.envio.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI envioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Envíos - BiblioGo")
                        .description("API REST para la gestión de envíos y entregas de libros del sistema BiblioGo")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BiblioGo Team")
                                .email("soporte@bibliogo.com")
                        )
                );
    }
}