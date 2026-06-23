package com.bibliogo.pago.controller;

import com.bibliogo.pago.dto.PagoRequestDTO;
import com.bibliogo.pago.dto.PagoResponseDTO;
import com.bibliogo.pago.model.Pago;
import com.bibliogo.pago.service.PagoService;

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
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "API REST para la gestión de pagos y multas")
public class PagoController {

    @Autowired
    private PagoService service;

    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar pago por ID", description = "Retorna un pago específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar pagos por usuario", description = "Retorna todos los pagos de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar pagos por préstamo", description = "Retorna todos los pagos asociados a un préstamo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<Pago>> buscarPorPrestamo(@PathVariable Integer prestamoId) {
        return ResponseEntity.ok(service.buscarPorPrestamo(prestamoId));
    }

    @Operation(summary = "Buscar pagos por estado", description = "Retorna pagos filtrados por estado (pendiente, pagado, rechazado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @Operation(summary = "Buscar pagos por tipo", description = "Retorna pagos filtrados por tipo (multa, otros)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Pago>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @Operation(summary = "Crear pago", description = "Registra un nuevo pago, verificando que el préstamo exista mediante comunicación con prestamo-service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado"),
        @ApiResponse(responseCode = "503", description = "Servicio de préstamos no disponible")
    })
    @PostMapping("/crear")
    public ResponseEntity<PagoResponseDTO> crear(@Valid @RequestBody PagoRequestDTO pagoDTO) {
        PagoResponseDTO creado = service.crear(pagoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Confirmar pago", description = "Cambia el estado del pago a 'pagado'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago confirmado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "409", description = "El pago ya fue confirmado o rechazado")
    })
    @PutMapping("/confirmar/{id}")
    public ResponseEntity<PagoResponseDTO> confirmar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @Operation(summary = "Rechazar pago", description = "Cambia el estado del pago a 'rechazado'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago rechazado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "409", description = "El pago ya fue confirmado o rechazado")
    })
    @PutMapping("/rechazar/{id}")
    public ResponseEntity<PagoResponseDTO> rechazar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.rechazar(id));
    }

    @Operation(summary = "Eliminar pago", description = "Elimina un pago del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Pago eliminado correctamente");
    }
}