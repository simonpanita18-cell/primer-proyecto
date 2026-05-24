package com.bibliogo.notificacion;

import com.bibliogo.notificacion.model.Notificacion;
import com.bibliogo.notificacion.repository.NotificacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(NotificacionRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Notificacion(null, 1, "prestamo", "Tu préstamo fue creado correctamente", "pendiente", LocalDateTime.now()));
                repository.save(new Notificacion(null, 1, "devolucion", "Recuerda devolver tu libro pronto", "pendiente", LocalDateTime.now()));
                repository.save(new Notificacion(null, 2, "sistema", "Bienvenido a BiblioGo", "leida", LocalDateTime.now()));
            }
        };
    }
}