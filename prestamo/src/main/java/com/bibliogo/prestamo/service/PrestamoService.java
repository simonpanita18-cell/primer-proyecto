package com.bibliogo.prestamo.service;

import com.bibliogo.prestamo.dto.PrestamoRequestDTO;
import com.bibliogo.prestamo.dto.PrestamoResponseDTO;
import com.bibliogo.prestamo.dto.PrestamoUpdateDTO;
import com.bibliogo.prestamo.exeption.ConflictoException;
import com.bibliogo.prestamo.exeption.RecursoNoEncontradoException;
import com.bibliogo.prestamo.exeption.ServicioNoDisponibleException;
import com.bibliogo.prestamo.model.Prestamo;
import com.bibliogo.prestamo.repository.PrestamoRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository repository;

    @Autowired
    private WebClient webClient;

    //  VERIFICACIONES 

    // Verifica que el usuario existe en UsuariosMicro
    private void verificarUsuario(Integer usuarioId) {
        try {
            webClient.get()
                    .uri("http://usuarios-micro/usuarios/" + usuarioId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException(
                    "Usuario no encontrado con id: " + usuarioId
            );
        } catch (WebClientRequestException e) {
            throw new ServicioNoDisponibleException(
                    "UsuariosMicro no está disponible. No se pudo verificar el usuario con id: " + usuarioId
            );
        } catch (Exception e) {
            throw new ServicioNoDisponibleException(
                    "Error al comunicarse con UsuariosMicro para verificar el usuario con id: " + usuarioId
            );
        }
    }

    // Verifica que el libro existe y tiene stock disponible
    private void verificarLibro(Integer libroId) {
        try {
            String respuesta = webClient.get()
                    .uri("http://catalogo-service/libros/" + libroId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (respuesta != null && respuesta.contains("no disponible")) {
                throw new ConflictoException(
                        "El libro no tiene stock disponible para préstamo"
                );
            }

        } catch (ConflictoException e) {
            // Relanzar excepciones de negocio sin envolver
            throw e;

        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException(
                    "Libro no encontrado con id: " + libroId
            );

        } catch (WebClientRequestException e) {
            throw new ServicioNoDisponibleException(
                    "catalogo-service no está disponible. No se pudo verificar el libro con id: " + libroId
            );

        } catch (Exception e) {
            throw new ServicioNoDisponibleException(
                    "Error al comunicarse con catalogo-service para verificar el libro con id: " + libroId
            );
        }
    }

    // REGLA DE NEGOCIO: reduce el stock al crear un préstamo
    private void reducirStockLibro(Integer libroId) {
        try {
            webClient.put()
                    .uri("http://catalogo-service/libros/reducir-stock/" + libroId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Stock reducido correctamente para libro id: {}", libroId);

        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException(
                    "Libro no encontrado al intentar reducir stock, id: " + libroId
            );
        } catch (WebClientResponseException.Conflict e) {
            throw new ConflictoException(
                    "No hay stock disponible para el libro con id: " + libroId
            );
        } catch (WebClientRequestException e) {
            throw new ServicioNoDisponibleException(
                    "catalogo-service no está disponible al reducir stock del libro id: " + libroId
            );
        } catch (Exception e) {
            throw new ServicioNoDisponibleException(
                    "Error al reducir stock del libro con id: " + libroId
            );
        }
    }

    // REGLA DE NEGOCIO: aumenta el stock al devolver un préstamo
    private void aumentarStockLibro(Integer libroId) {
        try {
            webClient.put()
                    .uri("http://catalogo-service/libros/aumentar-stock/" + libroId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Stock aumentado correctamente para libro id: {}", libroId);

        } catch (WebClientRequestException e) {
            throw new ServicioNoDisponibleException(
                    "catalogo-service no está disponible al aumentar stock del libro id: " + libroId
            );
        } catch (Exception e) {
            throw new ServicioNoDisponibleException(
                    "Error al aumentar stock del libro con id: " + libroId
            );
        }
    }

    // CRUD

    public List<PrestamoResponseDTO> listar() {
        log.info("Listando todos los préstamos");
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public PrestamoResponseDTO buscarPorId(Integer id) {
        log.info("Buscando préstamo con id: {}", id);
        Prestamo prestamo = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Préstamo no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Préstamo no encontrado con id: " + id
                    );
                });
        return convertirDTO(prestamo);
    }

    public List<PrestamoResponseDTO> buscarPorUsuario(Integer usuarioId) {
        log.info("Buscando préstamos del usuario id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public List<PrestamoResponseDTO> buscarPorEstado(String estado) {
        log.info("Buscando préstamos con estado: {}", estado);
        return repository.findByEstado(estado)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public List<PrestamoResponseDTO> prestamosActivosDeUsuario(Integer usuarioId) {
        log.info("Buscando préstamos activos del usuario id: {}", usuarioId);
        return repository.findByUsuarioIdAndEstado(usuarioId, "activo")
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public PrestamoResponseDTO crear(PrestamoRequestDTO dto) {
        log.info("Creando préstamo — usuario: {} libro: {}", dto.getUsuarioId(), dto.getLibroId());

        // Verificar que el usuario y libro existen
        verificarUsuario(dto.getUsuarioId());
        verificarLibro(dto.getLibroId());

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuarioId(dto.getUsuarioId());
        prestamo.setLibroId(dto.getLibroId());
        prestamo.setTituloLibro(dto.getTituloLibro());
        prestamo.setObservaciones(dto.getObservaciones());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
        prestamo.setEstado("activo");

        Prestamo guardado = repository.save(prestamo);

        // REGLA DE NEGOCIO: reducir stock al crear préstamo
        reducirStockLibro(dto.getLibroId());

        log.info("Préstamo creado con id: {}", guardado.getId());
        return convertirDTO(guardado);
    }

    public PrestamoResponseDTO devolver(Integer id) {
        log.info("Procesando devolución del préstamo id: {}", id);

        Prestamo prestamo = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Préstamo no encontrado con id: " + id
                ));

        if (prestamo.getEstado().equalsIgnoreCase("devuelto")
                || prestamo.getEstado().equalsIgnoreCase("devuelto con retraso")) {
            throw new ConflictoException("El préstamo ya fue devuelto");
        }

        prestamo.setFechaDevolucionReal(LocalDate.now());

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucion())) {
            prestamo.setEstado("devuelto con retraso");
        } else {
            prestamo.setEstado("devuelto");
        }

        Prestamo actualizado = repository.save(prestamo);

        // REGLA DE NEGOCIO: aumentar stock al devolver préstamo
        aumentarStockLibro(prestamo.getLibroId());

        log.info("Préstamo id: {} devuelto con estado: {}", id, actualizado.getEstado());
        return convertirDTO(actualizado);
    }

    public PrestamoResponseDTO actualizar(Integer id, PrestamoUpdateDTO dto) {
        log.info("Actualizando préstamo id: {}", id);

        Prestamo prestamo = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Préstamo no encontrado con id: " + id
                ));

        prestamo.setEstado(dto.getEstado());
        prestamo.setObservaciones(dto.getObservaciones());

        Prestamo actualizado = repository.save(prestamo);

        log.info("Préstamo id: {} actualizado correctamente", id);
        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando préstamo con id: {}", id);

        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Préstamo no encontrado con id: " + id
            );
        }

        repository.deleteById(id);
        log.info("Préstamo con id: {} eliminado correctamente", id);
    }

    // CONVERSIÓN 

    private PrestamoResponseDTO convertirDTO(Prestamo prestamo) {
        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getUsuarioId(),
                prestamo.getLibroId(),
                prestamo.getTituloLibro(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado()
        );
    }
}