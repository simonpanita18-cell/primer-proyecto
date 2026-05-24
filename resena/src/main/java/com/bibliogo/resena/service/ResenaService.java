package com.bibliogo.resena.service;

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

    public Resena crear(Resena resena) {
        resena.setCreadoEn(LocalDateTime.now());
        return repository.save(resena);
    }

    public Resena actualizar(Integer id, Resena datos) {
        Resena resena = buscarPorId(id);
        resena.setCalificacion(datos.getCalificacion());
        resena.setComentario(datos.getComentario());
        return repository.save(resena);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }
}