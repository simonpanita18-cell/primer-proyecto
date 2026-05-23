package com.bibliogo.catalogo_service;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Repository.LibroRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(LibroRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Libro(null, "El Quijote", "Miguel de Cervantes", "Clásico", "978-84-01-01", 5, "disponible", "La obra más importante de la literatura española", 1805));
                repository.save(new Libro(null, "Cien años de soledad", "Gabriel García Márquez", "Novela", "978-84-01-02", 3, "disponible", "Obra maestra del realismo mágico", 1967));
                repository.save(new Libro(null, "1984", "George Orwell", "Ciencia Ficción", "978-84-01-03", 0, "no disponible", "Novela distópica sobre el totalitarismo", 1949));
            }
        };
    }
}