package com.bibliogo.catalogo_service.Service;

import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Repository.LibroRepository;
import com.bibliogo.catalogo_service.dto.LibroRequestDTO;
import com.bibliogo.catalogo_service.dto.LibroResponseDTO;

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

    public LibroResponseDTO crear(LibroRequestDTO dto) {
        // REGLA DE NEGOCIO: no se puede crear con stock negativo
        if (dto.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setCategoria(dto.getCategoria());
        libro.setIsbn(dto.getIsbn());
        libro.setStock(dto.getStock());
        libro.setDescripcion(dto.getDescripcion());
        libro.setAnioPublicacion(dto.getAnioPublicacion());

        // REGLA DE NEGOCIO: disponibilidad automática según stock
        if (dto.getStock() > 0) {
            libro.setDisponibilidad("disponible");
        } else {
            libro.setDisponibilidad("no disponible");
        }

        Libro guardado = repository.save(libro);
        return convertirDTO(guardado);
    }

    public LibroResponseDTO actualizar(Integer id, LibroRequestDTO dto) {
        Libro libro = buscarPorId(id);
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setCategoria(dto.getCategoria());
        libro.setIsbn(dto.getIsbn());
        libro.setStock(dto.getStock());
        libro.setDescripcion(dto.getDescripcion());
        libro.setAnioPublicacion(dto.getAnioPublicacion());

        // REGLA DE NEGOCIO: disponibilidad automática según stock
        if (dto.getStock() > 0) {
            libro.setDisponibilidad("disponible");
        } else {
            libro.setDisponibilidad("no disponible");
        }

        Libro actualizado = repository.save(libro);
        return convertirDTO(actualizado);
    }

    // REGLA DE NEGOCIO: reducir stock al hacer préstamo
    public Libro reducirStock(Integer id) {
        Libro libro = buscarPorId(id);
        if (libro.getStock() <= 0) {
            throw new RuntimeException("No hay stock disponible para el libro: " + libro.getTitulo());
        }
        libro.setStock(libro.getStock() - 1);
        if (libro.getStock() == 0) {
            libro.setDisponibilidad("no disponible");
        }
        return repository.save(libro);
    }

    // REGLA DE NEGOCIO: aumentar stock al devolver préstamo
    public Libro aumentarStock(Integer id) {
        Libro libro = buscarPorId(id);
        libro.setStock(libro.getStock() + 1);
        libro.setDisponibilidad("disponible");
        return repository.save(libro);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    private LibroResponseDTO convertirDTO(Libro libro) {
        return new LibroResponseDTO(
            libro.getId(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getCategoria(),
            libro.getIsbn(),
            libro.getStock(),
            libro.getDisponibilidad(),
            libro.getDescripcion(),
            libro.getAnioPublicacion()
        );
    }
}