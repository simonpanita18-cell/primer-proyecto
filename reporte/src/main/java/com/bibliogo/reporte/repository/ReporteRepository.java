package com.bibliogo.reporte.repository;

import com.bibliogo.reporte.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {

    List<Reporte> findByTipo(String tipo);

    List<Reporte> findByGeneradoPor(String generadoPor);
}