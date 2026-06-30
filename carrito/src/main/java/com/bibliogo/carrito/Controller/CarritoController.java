package com.bibliogo.carrito.Controller;

import com.bibliogo.carrito.Model.Carrito;
import com.bibliogo.carrito.Service.CarritoService;
import com.bibliogo.carrito.dto.CarritoCantidadDTO;
import com.bibliogo.carrito.dto.CarritoRequestDTO;
import com.bibliogo.carrito.dto.CarritoResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
@Tag(name = "Carrito", description = "API REST para la gestión del carrito de reserva de libros")
public class CarritoController {

    @Autowired
    private CarritoService service;

    @Operation(summary = "Listar items de carrito", description = "Obtiene todos los items de carrito registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Carrito>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar item por ID", description = "Retorna un item de carrito específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar carrito por usuario", description = "Retorna todos los items de carrito de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Carrito>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Carrito activo del usuario", description = "Retorna los items activos del carrito de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}/activo")
    public ResponseEntity<List<Carrito>> carritoActivo(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.carritoActivoDeUsuario(usuarioId));
    }

    @Operation(summary = "Agregar libro al carrito", description = "Agrega un libro al carrito de reserva, verificando disponibilidad en catalogo-service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "409", description = "El libro no está disponible")
    })
    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponseDTO> agregar(@Valid @RequestBody CarritoRequestDTO carritoDTO) {
        CarritoResponseDTO creado = service.agregar(carritoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar cantidad", description = "Actualiza la cantidad de un item del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @PutMapping("/cantidad/{id}")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(
            @PathVariable Integer id,
            @Valid @RequestBody CarritoCantidadDTO cantidadDTO) {

        return ResponseEntity.ok(service.actualizarCantidad(id, cantidadDTO));
    }

    @Operation(summary = "Confirmar item de carrito", description = "Cambia el estado de un item de carrito a confirmado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item confirmado correctamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @PutMapping("/confirmar/{id}")
    public ResponseEntity<CarritoResponseDTO> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @Operation(summary = "Eliminar item de carrito", description = "Elimina un item específico del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Item eliminado del carrito");
    }

    @Operation(summary = "Vaciar carrito", description = "Cancela todos los items activos del carrito de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito vaciado correctamente")
    })
    @PutMapping("/vaciar/{usuarioId}")
    public ResponseEntity<String> vaciar(@PathVariable Integer usuarioId) {
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.ok("Carrito vaciado correctamente");
    }
}