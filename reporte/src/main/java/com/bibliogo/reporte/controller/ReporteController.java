package com.bibliogo.reporte.controller;

import com.bibliogo.reporte.dto.ReporteRequestDTO;
import com.bibliogo.reporte.dto.ReporteResponseDTO;
import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.service.ReporteService;

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
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "API REST para la gestión de reportes administrativos")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @Operation(summary = "Listar reportes", description = "Obtiene todos los reportes generados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Reporte>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar reporte por ID", description = "Retorna un reporte específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar reportes por tipo", description = "Retorna reportes filtrados por tipo (préstamos, usuarios, libros, etc.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @Operation(summary = "Buscar reportes por generador", description = "Retorna reportes filtrados por quién los generó")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/generado-por/{generadoPor}")
    public ResponseEntity<List<Reporte>> buscarPorGeneradoPor(@PathVariable String generadoPor) {
        return ResponseEntity.ok(service.buscarPorGeneradoPor(generadoPor));
    }

    @Operation(summary = "Crear reporte", description = "Genera un nuevo reporte administrativo en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/crear")
    public ResponseEntity<ReporteResponseDTO> crear(
            @Valid @RequestBody ReporteRequestDTO reporteDTO) {

        ReporteResponseDTO creado = service.crear(reporteDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok("Reporte eliminado correctamente");
    }
}