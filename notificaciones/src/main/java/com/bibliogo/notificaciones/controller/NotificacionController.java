package com.bibliogo.notificaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.bibliogo.notificaciones.dto.NotificacionRequestDTO;
import com.bibliogo.notificaciones.dto.NotificacionResponseDTO;
import com.bibliogo.notificaciones.model.Notificacion;
import com.bibliogo.notificaciones.service.NotificacionService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificacion>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @GetMapping("/usuario/{usuarioId}/pendientes")
    public ResponseEntity<List<Notificacion>> buscarPendientes(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarNoLeidas(usuarioId));
    }

    @PostMapping("/crear")
    public ResponseEntity<NotificacionResponseDTO> crear(
            @Valid @RequestBody NotificacionRequestDTO dto) {

        NotificacionResponseDTO creada = service.crear(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/leer/{id}")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(
            @PathVariable Integer id) {

        return ResponseEntity.ok(service.marcarComoLeida(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Notificación eliminada correctamente");
    }
}