package com.bibliogo.envio.repository;

import com.bibliogo.envio.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {

    List<Envio> findByUsuarioId(Integer usuarioId);

    List<Envio> findByPrestamoId(Integer prestamoId);

    List<Envio> findByEstado(String estado);
}