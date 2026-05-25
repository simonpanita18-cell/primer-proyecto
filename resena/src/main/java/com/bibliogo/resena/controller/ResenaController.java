package com.bibliogo.resena.controller;

import com.bibliogo.resena.dto.ResenaRequestDTO;
import com.bibliogo.resena.dto.ResenaResponseDTO;
import com.bibliogo.resena.dto.ResenaUpdateDTO;
import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.service.ResenaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Resena>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Resena>> buscarPorLibro(@PathVariable Integer libroId) {
        return ResponseEntity.ok(service.buscarPorLibro(libroId));
    }

    @GetMapping("/calificacion/{calificacion}")
    public ResponseEntity<List<Resena>> buscarPorCalificacion(@PathVariable Integer calificacion) {
        return ResponseEntity.ok(service.buscarPorCalificacion(calificacion));
    }

    @PostMapping("/crear")
    public ResponseEntity<ResenaResponseDTO> crear(
            @Valid @RequestBody ResenaRequestDTO dto) {

        ResenaResponseDTO creada = service.crear(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResenaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ResenaUpdateDTO dto) {

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Reseña eliminada correctamente");
    }
}