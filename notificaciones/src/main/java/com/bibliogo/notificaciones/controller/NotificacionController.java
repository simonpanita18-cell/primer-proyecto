package com.bibliogo.notificacion.controller;

import com.bibliogo.notificacion.model.Notificacion;
import com.bibliogo.notificacion.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @GetMapping("/listar")
    public List<Notificacion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Notificacion> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Notificacion> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Notificacion> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Notificacion> buscarPorTipo(@PathVariable String tipo) {
        return service.buscarPorTipo(tipo);
    }

    @GetMapping("/usuario/{usuarioId}/pendientes")
    public List<Notificacion> buscarPendientes(@PathVariable Integer usuarioId) {
        return service.buscarNoLeidas(usuarioId);
    }

    @PostMapping("/crear")
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return service.crear(notificacion);
    }

    @PutMapping("/leer/{id}")
    public Notificacion marcarComoLeida(@PathVariable Integer id) {
        return service.marcarComoLeida(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Notificación eliminada correctamente";
    }
}