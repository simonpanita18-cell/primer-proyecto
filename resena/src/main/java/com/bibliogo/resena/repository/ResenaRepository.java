package com.bibliogo.resena.repository;

import com.bibliogo.resena.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {

    List<Resena> findByUsuarioId(Integer usuarioId);

    List<Resena> findByLibroId(Integer libroId);

    List<Resena> findByCalificacion(Integer calificacion);
}