package com.bibliogo.envio.controller;

import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @GetMapping("/listar")
    public List<Envio> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Envio> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Envio> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/prestamo/{prestamoId}")
    public List<Envio> buscarPorPrestamo(@PathVariable Integer prestamoId) {
        return service.buscarPorPrestamo(prestamoId);
    }

    @GetMapping("/estado/{estado}")
    public List<Envio> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @PostMapping("/crear")
    public Envio crear(@RequestBody Envio envio) {
        return service.crear(envio);
    }

    @PutMapping("/despachar/{id}")
    public Envio despachar(@PathVariable Integer id) {
        return service.despachar(id);
    }

    @PutMapping("/entregar/{id}")
    public Envio entregar(@PathVariable Integer id) {
        return service.entregar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Envío eliminado correctamente";
    }
}