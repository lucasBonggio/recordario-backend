package com.recordario;

import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.recordario.analisis.modelos.Capitulo;
import com.recordario.cartas.Carta;
import com.recordario.libro.Libro;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioRepositorio;
import com.recordario.usuarios.dto.CrearUsuarioDTO;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FlujoCompletoIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;


    @Test
    void flujoCompleto_crearUsuario_analizarLibro_iniciarSesion_responder_y_verProgreso() throws Exception {
        CrearUsuarioDTO usuarioRequest = new CrearUsuarioDTO("usuarioTest", "contraseñaTest", "email@gmail.com");

        mockMvc.perform(
                post("/api/v1/publico/usuarios/registro")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usuarioRequest))
        )
        .andExpect(status().isCreated())
        .andReturn();

        Optional<Usuario> usuario = usuarioRepositorio.findByNombreUsuario("usuarioTest");
        
        Long usuarioId = usuario.get().getId();

        Libro libro = libroDePrueba();

        mockMvc.perform(
                post("/api/v1/autenticacion/libros/analizar")
                    .with(user("usuarioTest"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(libro))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tarjetasCreadas").value(greaterThan(0)));

        assertTrue(
            usuarioRepositorio.countTarjetasTotales(usuarioId) > 0
        );

        MvcResult sesionResult = mockMvc.perform(
                post("/api/v1/autenticacion/repaso/iniciar")
                    .with(user("usuarioTest"))
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sesionId").exists())
        .andReturn();

        String sesionId = JsonPath.read(
                sesionResult.getResponse().getContentAsString(),
                "$.sesionId"
        );

        Cookie cookie = new Cookie("sesionId", sesionId);

        mockMvc.perform(
                post("/api/v1/autenticacion/repaso/sesion/responder")
                    .cookie(cookie)
                    .with(user("usuarioTest"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("5")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.feedBack.fueCorrecta").value(true));

        mockMvc.perform(
                get("/api/v1/autenticacion/usuario/progreso")
                .with(user("usuarioTest"))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tarjetasTotales").value(greaterThan(0)))
        .andExpect(jsonPath("$.tarjetasRepasadasHoy").value(1));
    }

    private Libro libroDePrueba() {
        Libro libro = new Libro();
        libro.setTitulo("Clean Code");

        Capitulo capitulo = new Capitulo();
        capitulo.setTitulo("Capítulo 1");

        Carta carta = new Carta();
        carta.setDescripcion("Los nombres de variables deben ser descriptivos");

        capitulo.getCartas().add(carta);
        libro.getCapitulos().add(capitulo);

        return libro;
    }
}
