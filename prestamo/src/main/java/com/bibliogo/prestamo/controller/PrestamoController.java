package com.bibliogo.prestamo.controller;

import com.bibliogo.prestamo.dto.PrestamoRequestDTO;
import com.bibliogo.prestamo.dto.PrestamoResponseDTO;
import com.bibliogo.prestamo.dto.PrestamoUpdateDTO;
import com.bibliogo.prestamo.service.PrestamoService;

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
@RequestMapping("/prestamos")
@Tag(name = "Préstamos", description = "API REST para la gestión de préstamos de libros")
public class PrestamoController {

    @Autowired
    private PrestamoService service;

    @Operation(summary = "Listar préstamos", description = "Obtiene todos los préstamos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<PrestamoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar préstamo por ID", description = "Retorna un préstamo específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Préstamo encontrado"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar préstamos por usuario", description = "Retorna todos los préstamos de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponseDTO>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar préstamos por estado", description = "Retorna préstamos filtrados por estado (activo, devuelto, devuelto con retraso)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PrestamoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @Operation(summary = "Préstamos activos del usuario", description = "Retorna solo los préstamos activos de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> prestamosActivos(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.prestamosActivosDeUsuario(usuarioId));
    }

    @Operation(summary = "Crear préstamo", description = "Registra un nuevo préstamo, verificando usuario y libro mediante comunicación con otros microservicios, y reduce el stock automáticamente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario o libro no encontrado"),
        @ApiResponse(responseCode = "409", description = "El libro no tiene stock disponible"),
        @ApiResponse(responseCode = "503", description = "Servicio de usuarios o catálogo no disponible")
    })
    @PostMapping("/crear")
    public ResponseEntity<PrestamoResponseDTO> crear(@Valid @RequestBody PrestamoRequestDTO prestamoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(prestamoDTO));
    }

    @Operation(summary = "Devolver préstamo", description = "Marca un préstamo como devuelto y aumenta el stock del libro automáticamente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Préstamo devuelto correctamente"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado"),
        @ApiResponse(responseCode = "409", description = "El préstamo ya fue devuelto")
    })
    @PutMapping("/devolver/{id}")
    public ResponseEntity<PrestamoResponseDTO> devolver(@PathVariable Integer id) {
        return ResponseEntity.ok(service.devolver(id));
    }

    @Operation(summary = "Actualizar préstamo", description = "Actualiza el estado y observaciones de un préstamo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Préstamo actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PrestamoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PrestamoUpdateDTO prestamoDTO) {
        return ResponseEntity.ok(service.actualizar(id, prestamoDTO));
    }

    @Operation(summary = "Eliminar préstamo", description = "Elimina un préstamo del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Préstamo eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Préstamo eliminado correctamente");
    }
}