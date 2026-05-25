package com.bibliogo.pago.controller;

import com.bibliogo.pago.dto.PagoRequestDTO;
import com.bibliogo.pago.dto.PagoResponseDTO;
import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.service.PagoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<Pago>> buscarPorPrestamo(@PathVariable Integer prestamoId) {
        return ResponseEntity.ok(service.buscarPorPrestamo(prestamoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Pago>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PostMapping("/crear")
    public ResponseEntity<PagoResponseDTO> crear(@Valid @RequestBody PagoRequestDTO pagoDTO) {
        PagoResponseDTO creado = service.crear(pagoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/confirmar/{id}")
    public ResponseEntity<PagoResponseDTO> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @PutMapping("/rechazar/{id}")
    public ResponseEntity<PagoResponseDTO> rechazar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.rechazar(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Pago eliminado correctamente");
    }
}