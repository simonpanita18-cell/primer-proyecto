package com.bibliogo.catalogo_service.Controller;

import com.bibliogo.catalogo_service.Model.Libro;
import com.bibliogo.catalogo_service.Service.LibroService;
import com.bibliogo.catalogo_service.dto.LibroRequestDTO;
import com.bibliogo.catalogo_service.dto.LibroResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@Tag(name = "Catálogo", description = "API REST para la gestión del catálogo de libros")
public class LibroController {

    @Autowired
    private LibroService service;

    @Operation(summary = "Listar libros", description = "Obtiene todos los libros del catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Libro>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar libro por ID", description = "Retorna un libro específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar libro por ISBN", description = "Retorna un libro según su código ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado con ese ISBN")
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Libro> buscarPorIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(service.buscarPorIsbn(isbn));
    }

    @Operation(summary = "Buscar libros por categoría", description = "Retorna libros que pertenecen a una categoría específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Libro>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @Operation(summary = "Buscar libros por autor", description = "Retorna libros de un autor específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Libro>> buscarPorAutor(@PathVariable String autor) {
        return ResponseEntity.ok(service.buscarPorAutor(autor));
    }

    @Operation(summary = "Buscar libros por título", description = "Retorna libros que coincidan parcialmente con el título")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{titulo}")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(service.buscarPorTitulo(titulo));
    }

    @Operation(summary = "Listar libros disponibles", description = "Retorna solo los libros con stock disponible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/disponibles")
    public ResponseEntity<List<Libro>> disponibles() {
        return ResponseEntity.ok(service.buscarDisponibles());
    }

    @Operation(summary = "Crear libro", description = "Registra un nuevo libro en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "El stock no puede ser negativo")
    })
    @PostMapping("/crear")
    public ResponseEntity<LibroResponseDTO> crear(@Valid @RequestBody LibroRequestDTO libroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(libroDTO));
    }

    @Operation(summary = "Actualizar libro", description = "Actualiza los datos de un libro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<LibroResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody LibroRequestDTO libroDTO) {
        return ResponseEntity.ok(service.actualizar(id, libroDTO));
    }

    @Operation(summary = "Reducir stock", description = "Regla de negocio: reduce el stock del libro al crear un préstamo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock reducido correctamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "409", description = "No hay stock disponible")
    })
    @PutMapping("/reducir-stock/{id}")
    public ResponseEntity<Libro> reducirStock(@PathVariable Integer id) {
        return ResponseEntity.ok(service.reducirStock(id));
    }

    @Operation(summary = "Aumentar stock", description = "Regla de negocio: aumenta el stock del libro al devolver un préstamo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock aumentado correctamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/aumentar-stock/{id}")
    public ResponseEntity<Libro> aumentarStock(@PathVariable Integer id) {
        return ResponseEntity.ok(service.aumentarStock(id));
    }

    @Operation(summary = "Eliminar libro", description = "Elimina un libro del catálogo según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Libro eliminado correctamente");
    }
}