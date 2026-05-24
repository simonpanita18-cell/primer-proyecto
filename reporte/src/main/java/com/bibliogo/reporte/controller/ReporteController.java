package com.bibliogo.reporte.controller;

import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @GetMapping("/listar")
    public ResponseEntity<List<Reporte>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @GetMapping("/generado-por/{generadoPor}")
    public ResponseEntity<List<Reporte>> buscarPorGeneradoPor(@PathVariable String generadoPor) {
        return ResponseEntity.ok(service.buscarPorGeneradoPor(generadoPor));
    }

    @PostMapping("/crear")
    public ResponseEntity<Reporte> crear(@RequestBody Reporte reporte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(reporte));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Reporte eliminado correctamente");
    }
}