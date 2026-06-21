package com.bibliogo.resena.service;

import com.bibliogo.resena.dto.ResenaRequestDTO;
import com.bibliogo.resena.dto.ResenaResponseDTO;
import com.bibliogo.resena.dto.ResenaUpdateDTO;
import com.bibliogo.resena.exception.RecursoNoEncontradoException;
import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.repository.ResenaRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repository;

    public List<Resena> listar() {
        log.info("Listando todas las reseñas");
        return repository.findAll();
    }

    public Resena buscarPorId(Integer id) {
        log.info("Buscando reseña con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reseña no encontrada con id: {}", id);
                    return new RecursoNoEncontradoException("Reseña no encontrada con id: " + id);
                });
    }

    public List<Resena> buscarPorUsuario(Integer usuarioId) {
        log.info("Buscando reseñas del usuario id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Resena> buscarPorLibro(Integer libroId) {
        log.info("Buscando reseñas del libro id: {}", libroId);
        return repository.findByLibroId(libroId);
    }

    public List<Resena> buscarPorCalificacion(Integer calificacion) {
        log.info("Buscando reseñas con calificación: {}", calificacion);
        return repository.findByCalificacion(calificacion);
    }

    public ResenaResponseDTO crear(ResenaRequestDTO dto) {
        log.info("Creando reseña — usuario: {} libro: {} calificación: {}",
                dto.getUsuarioId(), dto.getLibroId(), dto.getCalificacion());

        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setLibroId(dto.getLibroId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setCreadoEn(LocalDateTime.now());

        Resena guardada = repository.save(resena);

        log.info("Reseña creada con id: {}", guardada.getId());
        return convertirDTO(guardada);
    }

    public ResenaResponseDTO actualizar(Integer id, ResenaUpdateDTO dto) {
        log.info("Actualizando reseña con id: {}", id);

        Resena resena = buscarPorId(id);
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());

        Resena actualizada = repository.save(resena);

        log.info("Reseña id: {} actualizada correctamente", id);
        return convertirDTO(actualizada);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando reseña con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Reseña no encontrada con id: {}", id);
            throw new RecursoNoEncontradoException("Reseña no encontrada con id: " + id);
        }

        repository.deleteById(id);
        log.info("Reseña con id: {} eliminada correctamente", id);
    }

    private ResenaResponseDTO convertirDTO(Resena resena) {
        return new ResenaResponseDTO(
                resena.getId(),
                resena.getUsuarioId(),
                resena.getLibroId(),
                resena.getCalificacion(),
                resena.getComentario(),
                resena.getCreadoEn()
        );
    }
}