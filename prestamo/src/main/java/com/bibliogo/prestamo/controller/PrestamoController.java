package com.bibliogo.prestamo.controller;

import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.service.PrestamoService;
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
    public ResponseEntity<List<Prestamo>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Prestamo>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @GetMapping("/usuario/{usuarioId}/activos")
    public ResponseEntity<List<Prestamo>> prestamosActivos(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.prestamosActivosDeUsuario(usuarioId));
    }

    @PostMapping("/crear")
    public ResponseEntity<Prestamo> crear(@RequestBody Prestamo prestamo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(prestamo));
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<Prestamo> devolver(@PathVariable Integer id) {
        return ResponseEntity.ok(service.devolver(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Prestamo> actualizar(@PathVariable Integer id, @RequestBody Prestamo prestamo) {
        return ResponseEntity.ok(service.actualizar(id, prestamo));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Préstamo eliminado correctamente");
    }
}