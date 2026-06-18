package com.Usuarios.UsuariosMicro.Controller;

import com.Usuarios.UsuariosMicro.Service.UsuarioService;
import com.Usuarios.UsuariosMicro.dto.UsuarioRequestDTO;
import com.Usuarios.UsuariosMicro.dto.UsuarioResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "API REST para la gestión de usuarios de BiblioGo")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // GET: listar todos los usuarios
    @Operation(
        summary = "Listar usuarios",
        description = "Obtiene una lista con todos los usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET: buscar usuario por ID
    @Operation(
        summary = "Buscar usuario por ID",
        description = "Retorna un usuario específico según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // GET: buscar usuario por correo
    @Operation(
        summary = "Buscar usuario por correo",
        description = "Retorna un usuario específico según su correo electrónico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ese correo")
    })
    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(service.buscarPorCorreo(correo));
    }

    // GET: buscar usuarios por rol
    @Operation(
        summary = "Buscar usuarios por rol",
        description = "Retorna todos los usuarios que tienen un rol específico (ej: LECTOR, ADMIN)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(service.buscarPorRol(rol));
    }

    // GET: buscar usuarios por estado
    @Operation(
        summary = "Buscar usuarios por estado",
        description = "Retorna todos los usuarios con un estado específico (ej: activo, inactivo)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    // POST: crear nuevo usuario
    @Operation(
        summary = "Crear usuario",
        description = "Registra un nuevo usuario en el sistema. La contraseña se encripta automáticamente con BCrypt"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "El correo ya está registrado")
    })
    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        UsuarioResponseDTO creado = service.crear(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT: actualizar usuario existente
    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "409", description = "El correo ya está registrado por otro usuario")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        UsuarioResponseDTO actualizado = service.actualizar(id, usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE: eliminar usuario por ID
    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario del sistema según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}