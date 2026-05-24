package com.bibliogo.catalogo_service.Service;

import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository repository;

    public List<Libro> listar() {
        return repository.findAll();
    }

    public Libro buscarPorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }

    public Libro buscarPorIsbn(String isbn) {
        return repository.findByIsbn(isbn)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado con isbn: " + isbn));
    }

    public List<Libro> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public List<Libro> buscarPorAutor(String autor) {
        return repository.findByAutorIgnoreCase(autor);
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> buscarDisponibles() {
        return repository.findByDisponibilidad("disponible");
    }

    public Libro crear(Libro libro) {
        if (libro.getStock() > 0) {
            libro.setDisponibilidad("disponible");
        } else {
            libro.setDisponibilidad("no disponible");
        }
        return repository.save(libro);
    }

    public Libro actualizar(Integer id, Libro datos) {
        Libro libro = buscarPorId(id);
        libro.setTitulo(datos.getTitulo());
        libro.setAutor(datos.getAutor());
        libro.setCategoria(datos.getCategoria());
        libro.setStock(datos.getStock());
        libro.setDescripcion(datos.getDescripcion());
        libro.setAnioPublicacion(datos.getAnioPublicacion());
        if (datos.getStock() > 0) {
            libro.setDisponibilidad("disponible");
        } else {
            libro.setDisponibilidad("no disponible");
        }
        return repository.save(libro);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}