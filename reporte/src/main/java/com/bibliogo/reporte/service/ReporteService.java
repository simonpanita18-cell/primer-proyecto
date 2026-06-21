package com.bibliogo.reporte.service;

import com.bibliogo.reporte.dto.ReporteRequestDTO;
import com.bibliogo.reporte.dto.ReporteResponseDTO;
import com.bibliogo.reporte.exception.RecursoNoEncontradoException;
import com.bibliogo.reporte.model.Reporte;
import com.bibliogo.reporte.repository.ReporteRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repository;

    public List<Reporte> listar() {
        log.info("Listando todos los reportes");
        return repository.findAll();
    }

    public Reporte buscarPorId(Integer id) {
        log.info("Buscando reporte con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reporte no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("Reporte no encontrado con id: " + id);
                });
    }

    public List<Reporte> buscarPorTipo(String tipo) {
        log.info("Buscando reportes de tipo: {}", tipo);
        return repository.findByTipo(tipo);
    }

    public List<Reporte> buscarPorGeneradoPor(String generadoPor) {
        log.info("Buscando reportes generados por: {}", generadoPor);
        return repository.findByGeneradoPor(generadoPor);
    }

    public ReporteResponseDTO crear(ReporteRequestDTO dto) {
        log.info("Creando reporte de tipo: {} generado por: {}", dto.getTipo(), dto.getGeneradoPor());

        Reporte reporte = new Reporte();
        reporte.setTipo(dto.getTipo());
        reporte.setDatos(dto.getDatos());
        reporte.setGeneradoPor(dto.getGeneradoPor());
        reporte.setUrl(dto.getUrl());
        reporte.setGeneradoEn(LocalDateTime.now());

        Reporte guardado = repository.save(reporte);

        log.info("Reporte creado con id: {}", guardado.getId());
        return convertirDTO(guardado);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando reporte con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Reporte no encontrado con id: {}", id);
            throw new RecursoNoEncontradoException(
                    "Reporte no encontrado con id: " + id
            );
        }

        repository.deleteById(id);
        log.info("Reporte con id: {} eliminado correctamente", id);
    }

    private ReporteResponseDTO convertirDTO(Reporte reporte) {
        return new ReporteResponseDTO(
                reporte.getId(),
                reporte.getTipo(),
                reporte.getDatos(),
                reporte.getGeneradoPor(),
                reporte.getGeneradoEn(),
                reporte.getUrl()
        );
    }
}