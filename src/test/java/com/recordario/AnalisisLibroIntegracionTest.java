package com.recordario;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recordario.analisis.modelos.Capitulo;
import com.recordario.cartas.Carta;
import com.recordario.libro.Libro;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaRepositorio;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioRepositorio;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AnalisisLibroIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TarjetaRepositorio tarjetaRepositorio;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void analizarLibro_creaTarjetas_yPermiteIniciarSesionDeRepaso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuarioRepositorio.save(usuario);

        Libro libro = libroDePrueba();

        mockMvc.perform(
                post("/api/v1/autenticacion/libros/analizar")
                .with(user("usuarioTest"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(libro))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tarjetasCreadas").value(greaterThan(0)));

        List<Tarjeta> tarjetas = tarjetaRepositorio.findAllByUsuario(usuario);
        assertFalse(tarjetas.isEmpty());
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
