package com.bibliogo.resena.service;

import com.bibliogo.resena.dto.ResenaRequestDTO;
import com.bibliogo.resena.dto.ResenaResponseDTO;
import com.bibliogo.resena.dto.ResenaUpdateDTO;
import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.repository.ResenaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repository;

    public List<Resena> listar() {
        return repository.findAll();
    }

    public Resena buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));
    }

    public List<Resena> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Resena> buscarPorLibro(Integer libroId) {
        return repository.findByLibroId(libroId);
    }

    public List<Resena> buscarPorCalificacion(Integer calificacion) {
        return repository.findByCalificacion(calificacion);
    }

    public ResenaResponseDTO crear(ResenaRequestDTO dto) {

        Resena resena = new Resena();

        resena.setUsuarioId(dto.getUsuarioId());
        resena.setLibroId(dto.getLibroId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setCreadoEn(LocalDateTime.now());

        Resena guardada = repository.save(resena);

        return convertirDTO(guardada);
    }

    public ResenaResponseDTO actualizar(Integer id, ResenaUpdateDTO dto) {

        Resena resena = buscarPorId(id);

        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());

        Resena actualizada = repository.save(resena);

        return convertirDTO(actualizada);
    }

    public void eliminar(Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con id: " + id);
        }

        repository.deleteById(id);
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