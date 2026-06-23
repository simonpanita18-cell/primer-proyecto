package com.bibliogo.envio.controller;

import com.bibliogo.envio.dto.EnvioRequestDTO;
import com.bibliogo.envio.dto.EnvioResponseDTO;
import com.bibliogo.envio.model.Envio;
import com.bibliogo.envio.service.EnvioService;

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
@RequestMapping("/envios")
@Tag(name = "Envíos", description = "API REST para la gestión de envíos y entregas de libros")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @Operation(summary = "Listar envíos", description = "Obtiene todos los envíos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Envio>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar envío por ID", description = "Retorna un envío específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar envíos por usuario", description = "Retorna todos los envíos de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Envio>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar envíos por préstamo", description = "Retorna todos los envíos asociados a un préstamo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<Envio>> buscarPorPrestamo(@PathVariable Integer prestamoId) {
        return ResponseEntity.ok(service.buscarPorPrestamo(prestamoId));
    }

    @Operation(summary = "Buscar envíos por estado", description = "Retorna envíos filtrados por estado (pendiente, en camino, entregado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Envio>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @Operation(summary = "Crear envío", description = "Registra un nuevo envío, verificando que el préstamo exista mediante comunicación con prestamo-service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Préstamo no encontrado"),
        @ApiResponse(responseCode = "503", description = "Servicio de préstamos no disponible")
    })
    @PostMapping("/crear")
    public ResponseEntity<EnvioResponseDTO> crear(
            @Valid @RequestBody EnvioRequestDTO envioDTO) {

        EnvioResponseDTO creado = service.crear(envioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Despachar envío", description = "Cambia el estado del envío a 'en camino'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío despachado correctamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
        @ApiResponse(responseCode = "409", description = "El envío ya fue entregado o ya está en camino")
    })
    @PutMapping("/despachar/{id}")
    public ResponseEntity<EnvioResponseDTO> despachar(@PathVariable Integer id) {

        return ResponseEntity.ok(service.despachar(id));
    }

    @Operation(summary = "Entregar envío", description = "Marca el envío como entregado y registra la fecha de entrega")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío entregado correctamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
        @ApiResponse(responseCode = "409", description = "El envío ya fue entregado")
    })
    @PutMapping("/entregar/{id}")
    public ResponseEntity<EnvioResponseDTO> entregar(@PathVariable Integer id) {

        return ResponseEntity.ok(service.entregar(id));
    }

    @Operation(summary = "Eliminar envío", description = "Elimina un envío del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Envío eliminado correctamente");
    }
}