package com.bibliogo.carrito.Controller;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Carrito>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Carrito>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/activo")
    public ResponseEntity<List<Carrito>> carritoActivo(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.carritoActivoDeUsuario(usuarioId));
    }

    @PostMapping("/agregar")
    public ResponseEntity<Carrito> agregar(@RequestBody Carrito carrito) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agregar(carrito));
    }

    @PutMapping("/cantidad/{id}")
    public ResponseEntity<Carrito> actualizarCantidad(@PathVariable Integer id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(service.actualizarCantidad(id, cantidad));
    }

    @PutMapping("/confirmar/{id}")
    public ResponseEntity<Carrito> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Item eliminado del carrito");
    }

    @PutMapping("/vaciar/{usuarioId}")
    public ResponseEntity<String> vaciar(@PathVariable Integer usuarioId) {
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.ok("Carrito vaciado correctamente");
    }
}