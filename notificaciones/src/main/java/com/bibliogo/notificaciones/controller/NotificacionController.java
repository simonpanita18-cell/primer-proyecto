package com.bibliogo.notificaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.bibliogo.notificaciones.dto.NotificacionRequestDTO;
import com.bibliogo.notificaciones.dto.NotificacionResponseDTO;
import com.bibliogo.notificaciones.model.Notificacion;
import com.bibliogo.notificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificaciones", description = "API REST para la gestión de notificaciones a usuarios")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @Operation(summary = "Listar notificaciones", description = "Obtiene todas las notificaciones registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar notificación por ID", description = "Retorna una notificación específica según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar notificaciones por usuario", description = "Retorna todas las notificaciones de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar notificaciones por estado", description = "Retorna notificaciones filtradas por estado (pendiente, leída)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @Operation(summary = "Buscar notificaciones por tipo", description = "Retorna notificaciones filtradas por tipo (préstamo, devolución, sistema)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificacion>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @Operation(summary = "Notificaciones pendientes del usuario", description = "Retorna las notificaciones no leídas de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}/pendientes")
    public ResponseEntity<List<Notificacion>> buscarPendientes(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarNoLeidas(usuarioId));
    }

    @Operation(summary = "Crear notificación", description = "Registra una nueva notificación para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/crear")
    public ResponseEntity<NotificacionResponseDTO> crear(
            @Valid @RequestBody NotificacionRequestDTO dto) {

        NotificacionResponseDTO creada = service.crear(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Marcar como leída", description = "Cambia el estado de una notificación a 'leída'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "409", description = "La notificación ya fue marcada como leída")
    })
    @PutMapping("/leer/{id}")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(
            @PathVariable Integer id) {

        return ResponseEntity.ok(service.marcarComoLeida(id));
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Notificación eliminada correctamente");
    }
}