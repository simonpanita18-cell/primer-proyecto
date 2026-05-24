package com.bibliogo.reporte.controller;

import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @GetMapping("/listar")
    public List<Reporte> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Optional<Reporte> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Reporte> buscarPorTipo(@PathVariable String tipo) {
        return service.buscarPorTipo(tipo);
    }

    @GetMapping("/generado-por/{generadoPor}")
    public List<Reporte> buscarPorGeneradoPor(@PathVariable String generadoPor) {
        return service.buscarPorGeneradoPor(generadoPor);
    }

    @PostMapping("/crear")
    public Reporte crear(@RequestBody Reporte reporte) {
        return service.crear(reporte);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "Reporte eliminado correctamente";
    }
}