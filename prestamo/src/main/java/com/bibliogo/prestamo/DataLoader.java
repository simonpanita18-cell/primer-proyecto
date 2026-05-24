package com.bibliogo.prestamo;

import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.repository.PrestamoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(PrestamoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Prestamo(null, 1, 1, "El Quijote",
                        LocalDate.now(), LocalDate.now().plusDays(7),
                        null, "activo", "Sin observaciones"));

                repository.save(new Prestamo(null, 2, 2, "Cien años de soledad",
                        LocalDate.now().minusDays(10), LocalDate.now().minusDays(3),
                        LocalDate.now(), "devuelto con retraso", "Entregado tarde"));

                repository.save(new Prestamo(null, 1, 3, "1984",
                        LocalDate.now().minusDays(3), LocalDate.now().plusDays(4),
                        null, "activo", "Sin observaciones"));
            }
        };
    }
}