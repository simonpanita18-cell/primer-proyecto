package com.bibliogo.prestamo.controller;

import com.bibliogo.prestamo.dto.PrestamoRequestDTO;
import com.bibliogo.prestamo.dto.PrestamoResponseDTO;
import com.bibliogo.prestamo.dto.PrestamoUpdateDTO;
import com.bibliogo.prestamo.service.PrestamoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService service;

    @GetMapping("/listar")
    public ResponseEntity<List<PrestamoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponseDTO>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PrestamoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @GetMapping("/usuario/{usuarioId}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> prestamosActivos(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.prestamosActivosDeUsuario(usuarioId));
    }

    @PostMapping("/crear")
    public ResponseEntity<PrestamoResponseDTO> crear(@Valid @RequestBody PrestamoRequestDTO prestamoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(prestamoDTO));
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<PrestamoResponseDTO> devolver(@PathVariable Integer id) {
        return ResponseEntity.ok(service.devolver(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PrestamoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PrestamoUpdateDTO prestamoDTO) {
        return ResponseEntity.ok(service.actualizar(id, prestamoDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Préstamo eliminado correctamente");
    }
}