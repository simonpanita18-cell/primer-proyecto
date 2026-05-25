package com.bibliogo.envio.controller;

import com.bibliogo.envio.dto.EnvioRequestDTO;
import com.bibliogo.envio.dto.EnvioResponseDTO;
import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.service.EnvioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Envio>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Envio>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<Envio>> buscarPorPrestamo(@PathVariable Integer prestamoId) {
        return ResponseEntity.ok(service.buscarPorPrestamo(prestamoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Envio>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @PostMapping("/crear")
    public ResponseEntity<EnvioResponseDTO> crear(
            @Valid @RequestBody EnvioRequestDTO envioDTO) {

        EnvioResponseDTO creado = service.crear(envioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/despachar/{id}")
    public ResponseEntity<EnvioResponseDTO> despachar(@PathVariable Integer id) {

        return ResponseEntity.ok(service.despachar(id));
    }

    @PutMapping("/entregar/{id}")
    public ResponseEntity<EnvioResponseDTO> entregar(@PathVariable Integer id) {

        return ResponseEntity.ok(service.entregar(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Envío eliminado correctamente");
    }
}