package com.bibliogo.reporte.service;

import com.bibliogo.reporte.dto.ReporteRequestDTO;
import com.bibliogo.reporte.dto.ReporteResponseDTO;
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

    public ReporteResponseDTO crear(ReporteRequestDTO dto) {

        Reporte reporte = new Reporte();

        reporte.setTipo(dto.getTipo());
        reporte.setDatos(dto.getDatos());
        reporte.setGeneradoPor(dto.getGeneradoPor());
        reporte.setGeneradoEn(LocalDateTime.now());

        Reporte guardado = repository.save(reporte);

        return convertirDTO(guardado);
    }

    public void eliminar(Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con id: " + id);
        }

        repository.deleteById(id);
    }

    private ReporteResponseDTO convertirDTO(Reporte reporte) {

        return new ReporteResponseDTO(
                reporte.getId(),
                reporte.getTipo(),
                reporte.getDatos(),
                reporte.getGeneradoPor(),
                reporte.getGeneradoEn()
        );
    }
}