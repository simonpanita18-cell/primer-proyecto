package com.Usuarios.UsuariosMicro.Service;

import com.Usuarios.UsuariosMicro.Exception.ConflictoException;
import com.Usuarios.UsuariosMicro.Exception.RecursoNoEncontradoException;
import com.Usuarios.UsuariosMicro.Model.Usuario;
import com.Usuarios.UsuariosMicro.Repository.UsuarioRepository;
import com.Usuarios.UsuariosMicro.dto.UsuarioRequestDTO;
import com.Usuarios.UsuariosMicro.dto.UsuarioResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = buscarEntidadPorId(id);
        return convertirDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con correo: " + correo
                ));

        return convertirDTO(usuario);
    }

    public List<UsuarioResponseDTO> buscarPorRol(String rol) {
        return usuarioRepository.findByRol(rol)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public List<UsuarioResponseDTO> buscarPorEstado(String estado) {
        return usuarioRepository.findByEstado(estado)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {

        if (usuarioRepository.findByCorreoIgnoreCase(dto.getCorreo()).isPresent()) {
            throw new ConflictoException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());

        // REGLA DE NEGOCIO: encriptar contraseña con BCrypt
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(dto.getRol());
        usuario.setEstado("activo");

        Usuario guardado = usuarioRepository.save(usuario);

        return convertirDTO(guardado);
    }

    public UsuarioResponseDTO actualizar(Integer id, UsuarioRequestDTO dto) {

        Usuario usuario = buscarEntidadPorId(id);

        usuarioRepository.findByCorreoIgnoreCase(dto.getCorreo())
                .ifPresent(usuarioExistente -> {
                    if (!usuarioExistente.getId().equals(id)) {
                        throw new ConflictoException("El correo ya está registrado por otro usuario");
                    }
                });

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());

        // REGLA DE NEGOCIO: re-encriptar contraseña si se actualiza
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(dto.getRol());

        Usuario actualizado = usuarioRepository.save(usuario);

        return convertirDTO(actualizado);
    }

    public void eliminar(Integer id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
        }

        usuarioRepository.deleteById(id);
    }

    private Usuario buscarEntidadPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + id
                ));
    }

    private UsuarioResponseDTO convertirDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getEstado()
        );
    }
}