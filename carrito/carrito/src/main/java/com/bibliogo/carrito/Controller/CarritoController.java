package com.bibliogo.carrito.Controller;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService service;

    @GetMapping("/listar")
    public List<Carrito> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Carrito> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Carrito> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}/activo")
    public List<Carrito> carritoActivo(@PathVariable Integer usuarioId) {
        return service.carritoActivoDeUsuario(usuarioId);
    }

    @PostMapping("/agregar")
    public Carrito agregar(@RequestBody Carrito carrito) {
        return service.agregar(carrito);
    }

    @PutMapping("/cantidad/{id}")
    public Carrito actualizarCantidad(@PathVariable Integer id, @RequestParam Integer cantidad) {
        return service.actualizarCantidad(id, cantidad);
    }

    @PutMapping("/confirmar/{id}")
    public Carrito confirmar(@PathVariable Integer id) {
        return service.confirmar(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Item eliminado del carrito";
    }

    @PutMapping("/vaciar/{usuarioId}")
    public String vaciar(@PathVariable Integer usuarioId) {
        service.vaciarCarrito(usuarioId);
        return "Carrito vaciado correctamente";
    }
}