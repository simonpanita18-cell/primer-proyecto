package com.bibliogo.catalogo_service.Repository;

import com.bibliogo.catalogo_service.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;



@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {
    List<Libro> findByCategoria(String categoria);
    List<Libro> findByAutorIgnoreCase(String autor);
    Optional<Libro> findByIsbn(String isbn);
    List<Libro> findByDisponibilidad(String disponibilidad);
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
}