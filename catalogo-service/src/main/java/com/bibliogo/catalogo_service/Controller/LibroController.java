package com.bibliogo.catalogo_service.Controller;

import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Service.LibroService;
import com.bibliogo.catalogo_service.dto.LibroRequestDTO;
import com.bibliogo.catalogo_service.dto.LibroResponseDTO;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Libro>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Libro> buscarPorIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(service.buscarPorIsbn(isbn));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Libro>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Libro>> buscarPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(service.buscarPorAutor(autor));
    }

    @GetMapping("/buscar/{titulo}")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(service.buscarPorTitulo(titulo));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Libro>> disponibles() {
        return ResponseEntity.ok(service.buscarDisponibles());
    }

    @PostMapping("/crear")
    public ResponseEntity<LibroResponseDTO> crear(@Valid @RequestBody LibroRequestDTO libroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(libroDTO));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<LibroResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody LibroRequestDTO libroDTO) {
        return ResponseEntity.ok(service.actualizar(id, libroDTO));
    }

    // REGLA DE NEGOCIO: reducir stock al prestar
    @PutMapping("/reducir-stock/{id}")
    public ResponseEntity<Libro> reducirStock(@PathVariable Integer id) {
        return ResponseEntity.ok(service.reducirStock(id));
    }

    // REGLA DE NEGOCIO: aumentar stock al devolver
    @PutMapping("/aumentar-stock/{id}")
    public ResponseEntity<Libro> aumentarStock(@PathVariable Integer id) {
        return ResponseEntity.ok(service.aumentarStock(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Libro eliminado correctamente");
    }
}