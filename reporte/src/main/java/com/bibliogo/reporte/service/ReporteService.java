package com.bibliogo.reporte.service;

import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repository;

    public List<Reporte> listar() {
        return repository.findAll();
    }

    public Reporte buscarPorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
    }

    public List<Reporte> buscarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Reporte> buscarPorGeneradoPor(String generadoPor) {
        return repository.findByGeneradoPor(generadoPor);
    }

    public Reporte crear(Reporte reporte) {
        reporte.setGeneradoEn(LocalDateTime.now());
        return repository.save(reporte);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}