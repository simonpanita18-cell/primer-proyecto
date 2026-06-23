package com.bibliogo.resena.controller;

import com.bibliogo.resena.dto.ResenaRequestDTO;
import com.bibliogo.resena.dto.ResenaResponseDTO;
import com.bibliogo.resena.dto.ResenaUpdateDTO;
import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.service.ResenaService;

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
@RequestMapping("/resenas")
@Tag(name = "Reseñas", description = "API REST para la gestión de reseñas y calificaciones de libros")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @Operation(summary = "Listar reseñas", description = "Obtiene todas las reseñas registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Resena>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar reseña por ID", description = "Retorna una reseña específica según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar reseñas por usuario", description = "Retorna todas las reseñas de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar reseñas por libro", description = "Retorna todas las reseñas de un libro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Resena>> buscarPorLibro(@PathVariable Integer libroId) {
        return ResponseEntity.ok(service.buscarPorLibro(libroId));
    }

    @Operation(summary = "Buscar reseñas por calificación", description = "Retorna reseñas filtradas por una calificación específica (1 a 5)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/calificacion/{calificacion}")
    public ResponseEntity<List<Resena>> buscarPorCalificacion(@PathVariable Integer calificacion) {
        return ResponseEntity.ok(service.buscarPorCalificacion(calificacion));
    }

    @Operation(summary = "Crear reseña", description = "Registra una nueva reseña con calificación y comentario para un libro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/crear")
    public ResponseEntity<ResenaResponseDTO> crear(
            @Valid @RequestBody ResenaRequestDTO dto) {

        ResenaResponseDTO creada = service.crear(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Actualizar reseña", description = "Actualiza la calificación y comentario de una reseña existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResenaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ResenaUpdateDTO dto) {

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Reseña eliminada correctamente");
    }
}