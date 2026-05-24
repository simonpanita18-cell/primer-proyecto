package com.bibliogo.pago.controller;

import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService service;

    @GetMapping("/listar")
    public List<Pago> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Pago> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pago> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/prestamo/{prestamoId}")
    public List<Pago> buscarPorPrestamo(@PathVariable Integer prestamoId) {
        return service.buscarPorPrestamo(prestamoId);
    }

    @GetMapping("/estado/{estado}")
    public List<Pago> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Pago> buscarPorTipo(@PathVariable String tipo) {
        return service.buscarPorTipo(tipo);
    }

    @PostMapping("/crear")
    public Pago crear(@RequestBody Pago pago) {
        return service.crear(pago);
    }

    @PutMapping("/confirmar/{id}")
    public Pago confirmar(@PathVariable Integer id) {
        return service.confirmar(id);
    }

    @PutMapping("/rechazar/{id}")
    public Pago rechazar(@PathVariable Integer id) {
        return service.rechazar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Pago eliminado correctamente";
    }
}