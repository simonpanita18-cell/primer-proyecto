package com.bibliogo.resena.controller;

import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @GetMapping("/listar")
    public List<Resena> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Resena> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Resena> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/libro/{libroId}")
    public List<Resena> buscarPorLibro(@PathVariable Integer libroId) {
        return service.buscarPorLibro(libroId);
    }

    @GetMapping("/calificacion/{calificacion}")
    public List<Resena> buscarPorCalificacion(@PathVariable Integer calificacion) {
        return service.buscarPorCalificacion(calificacion);
    }

    @PostMapping("/crear")
    public Resena crear(@RequestBody Resena resena) {
        return service.crear(resena);
    }

    @PutMapping("/actualizar/{id}")
    public Resena actualizar(@PathVariable Integer id, @RequestBody Resena resena) {
        return service.actualizar(id, resena);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Reseña eliminada correctamente";
    }
}