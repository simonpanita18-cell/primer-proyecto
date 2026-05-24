package com.bibliogo.prestamo.controller;

import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService service;

    @GetMapping("/listar")
    public List<Prestamo> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Prestamo> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Prestamo> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Prestamo> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @GetMapping("/usuario/{usuarioId}/activos")
    public List<Prestamo> prestamosActivos(@PathVariable Integer usuarioId) {
        return service.prestamosActivosDeUsuario(usuarioId);
    }

    @PostMapping("/crear")
    public Prestamo crear(@RequestBody Prestamo prestamo) {
        return service.crear(prestamo);
    }

    @PutMapping("/devolver/{id}")
    public Prestamo devolver(@PathVariable Integer id) {
        return service.devolver(id);
    }

    @PutMapping("/actualizar/{id}")
    public Prestamo actualizar(@PathVariable Integer id, @RequestBody Prestamo prestamo) {
        return service.actualizar(id, prestamo);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Préstamo eliminado correctamente";
    }
}