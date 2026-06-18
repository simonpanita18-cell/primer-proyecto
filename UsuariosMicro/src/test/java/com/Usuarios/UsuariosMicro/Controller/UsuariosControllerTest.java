package com.Usuarios.UsuariosMicro.Controller;

import com.Usuarios.UsuariosMicro.Service.UsuarioService;
import com.Usuarios.UsuariosMicro.dto.UsuarioResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    @Test
    @WithMockUser(username = "simon@bibliogo.com", roles = "LECTOR")
    void listarUsuarios() throws Exception {

        List<UsuarioResponseDTO> usuarios = List.of(
            new UsuarioResponseDTO(
                1, "Simon", "Hercules",
                "simon@bibliogo.com", "912345678",
                "Santiago", "LECTOR", "activo"
            )
        );

        when(service.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios/listar"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].nombre").value("Simon"))
               .andExpect(jsonPath("$[0].correo").value("simon@bibliogo.com"))
               .andExpect(jsonPath("$[0].rol").value("LECTOR"));
    }

    @Test
    @WithMockUser(username = "simon@bibliogo.com", roles = "LECTOR")
    void buscarPorId() throws Exception {

        UsuarioResponseDTO usuario = new UsuarioResponseDTO(
            1, "Simon", "Hercules",
            "simon@bibliogo.com", "912345678",
            "Santiago", "LECTOR", "activo"
        );

        when(service.buscarPorId(1)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre").value("Simon"))
               .andExpect(jsonPath("$.correo").value("simon@bibliogo.com"));
    }

    @Test
    @WithMockUser(username = "simon@bibliogo.com", roles = "LECTOR")
    void buscarPorRol() throws Exception {

        List<UsuarioResponseDTO> lectores = List.of(
            new UsuarioResponseDTO(
                1, "Simon", "Hercules",
                "simon@bibliogo.com", "912345678",
                "Santiago", "LECTOR", "activo"
            )
        );

        when(service.buscarPorRol("LECTOR")).thenReturn(lectores);

        mockMvc.perform(get("/usuarios/rol/LECTOR"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].rol").value("LECTOR"));
    }
}