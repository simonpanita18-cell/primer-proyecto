package com.bibliogo.prestamo.repository;

import com.bibliogo.prestamo.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    List<Prestamo> findByUsuarioId(Integer usuarioId);
    List<Prestamo> findByEstado(String estado);
    List<Prestamo> findByLibroId(Integer libroId);
    List<Prestamo> findByUsuarioIdAndEstado(Integer usuarioId, String estado);
}