package com.recordario;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaRepositorio;
import com.recordario.tema.Tema;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioRepositorio;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AnalisisTemaIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TarjetaRepositorio tarjetaRepositorio;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void analizarTema_creaTarjetas_yPermiteIniciarSesionDeRepaso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        usuarioRepositorio.save(usuario);

        Tema tema = temaDePrueba();

        MvcResult resultado = mockMvc.perform(
                post("/api/v1/autenticacion/tarjetas/")
                .with(user("usuarioTest"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(tema))
            )
            .andExpect(status().isOk())
            .andReturn();

        String mensaje = resultado.getResponse().getContentAsString();

        assertEquals("Tarjeta creada satisfactoriamente.", mensaje);

        List<Tarjeta> tarjetas = tarjetaRepositorio.findAllByUsuario(usuario);
        assertFalse(tarjetas.isEmpty());
    }

    private Tema temaDePrueba(){
        List<String> ideasPrincipales = List.of("POO", "Java");
        Tema prueba = new Tema();

        prueba.setTitulo("Programación");
        prueba.setIdeasPrincipales(ideasPrincipales);
        return prueba;
        
    }
}
