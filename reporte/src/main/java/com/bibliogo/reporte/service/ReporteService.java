package com.bibliogo.reporte.service;

import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repository;

    public List<Reporte> listar() {
        return repository.findAll();
    }

    public Optional<Reporte> buscarPorId(Integer id) {
        return repository.findById(id);
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
        repository.deleteById(id);
    }
}