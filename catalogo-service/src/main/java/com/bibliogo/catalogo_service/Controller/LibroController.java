package com.bibliogo.catalogo_service.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Service.LibroService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService service;

    @GetMapping("/listar")
    public List<Libro> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Libro> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/isbn/{isbn}")
    public Optional<Libro> buscarPorIsbn(@PathVariable String isbn) {
        return service.buscarPorIsbn(isbn);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Libro> buscarPorCategoria(@PathVariable String categoria) {
        return service.buscarPorCategoria(categoria);
    }

    @GetMapping("/autor/{autor}")
    public List<Libro> buscarPorAutor(@PathVariable String autor) {
        return service.buscarPorAutor(autor);
    }

    @GetMapping("/buscar/{titulo}")
    public List<Libro> buscarPorTitulo(@PathVariable String titulo) {
        return service.buscarPorTitulo(titulo);
    }

    @GetMapping("/disponibles")
    public List<Libro> disponibles() {
        return service.buscarDisponibles();
    }

    @PostMapping("/crear")
    public Libro crear(@RequestBody Libro libro) {
        return service.crear(libro);
    }

    @PutMapping("/actualizar/{id}")
    public Libro actualizar(@PathVariable Integer id, @RequestBody Libro libro) {
        return service.actualizar(id, libro);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Libro eliminado correctamente";
    }
}
