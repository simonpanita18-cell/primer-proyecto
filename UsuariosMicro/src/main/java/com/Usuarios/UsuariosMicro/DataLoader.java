package com.Usuarios.UsuariosMicro;

import com.Usuarios.UsuariosMicro.Model.Usuario;
import com.Usuarios.UsuariosMicro.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Usuario(null, "Juan", "Pérez", "juan@mail.com",
                    passwordEncoder.encode("123456"),
                    "912345678", "Av. Principal 123", "admin", "activo"));

                repository.save(new Usuario(null, "María", "González", "maria@mail.com",
                    passwordEncoder.encode("123456"),
                    "987654321", "Calle Sur 456", "bibliotecario", "activo"));

                repository.save(new Usuario(null, "Carlos", "López", "carlos@mail.com",
                    passwordEncoder.encode("123456"),
                    "956789012", "Pasaje Norte 789", "lector", "activo"));
            }
        };
    }
}