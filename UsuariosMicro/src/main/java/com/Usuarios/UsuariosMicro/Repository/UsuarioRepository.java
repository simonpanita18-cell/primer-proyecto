package com.Usuarios.UsuariosMicro.Repository;

import com.Usuarios.UsuariosMicro.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    List<Usuario> findByRol(String rol);
    List<Usuario> findByEstado(String estado);
}