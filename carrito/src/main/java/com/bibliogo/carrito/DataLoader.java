package com.bibliogo.carrito;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Repository.CarritoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(CarritoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Carrito(null, 1, 1, "El Quijote", 2, "activo"));
                repository.save(new Carrito(null, 1, 2, "Cien años de soledad", 1, "activo"));
                repository.save(new Carrito(null, 2, 3, "1984", 1, "confirmado"));
            }
        };
    }
}