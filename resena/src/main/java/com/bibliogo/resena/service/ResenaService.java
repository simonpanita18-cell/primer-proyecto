package com.bibliogo.resena.service;

import com.bibliogo.resena.model.Resena;
import com.bibliogo.resena.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repository;

    public List<Resena> listar() {
        return repository.findAll();
    }

    public Optional<Resena> buscarPorId(Integer id) {
        return repository.findById(id);
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
        Resena resena = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));

        resena.setCalificacion(datos.getCalificacion());
        resena.setComentario(datos.getComentario());

        return repository.save(resena);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}