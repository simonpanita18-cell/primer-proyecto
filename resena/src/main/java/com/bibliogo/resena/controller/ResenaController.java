package com.bibliogo.resena.controller;

import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.service.ResenaService;
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
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(resena));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Resena> actualizar(@PathVariable Integer id, @RequestBody Resena resena) {
        return ResponseEntity.ok(service.actualizar(id, resena));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }
}