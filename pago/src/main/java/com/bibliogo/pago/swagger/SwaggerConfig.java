package com.bibliogo.pago.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pagoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pagos - BiblioGo")
                        .description("API REST para la gestión de pagos y multas del sistema BiblioGo")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BiblioGo Team")
                                .email("soporte@bibliogo.com")
                        )
                );
    }
}