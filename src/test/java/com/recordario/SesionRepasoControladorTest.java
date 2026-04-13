package com.recordario;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.recordario.compartido.enums.TipoCarta;
import com.recordario.excepciones.ManejarExcepcion;
import com.recordario.excepciones.tipos.DatosInvalidos;
import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.excepciones.tipos.SesionFinalizada;
import com.recordario.repaso.SesionRepasoServicio;
import com.recordario.repaso.dto.EstadoSesionDTO;
import com.recordario.repaso.dto.FeedBack;
import com.recordario.repaso.dto.RespuestaTarjetaDTO;
import com.recordario.repaso.dto.SesionInicioDTO;
import com.recordario.tarjetas.dto.TarjetaDTO;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ManejarExcepcion.class)
public class SesionRepasoControladorTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SesionRepasoServicio sesionServicio;

    @Test
    void iniciarSesion_devuelve200() throws Exception {
        TarjetaDTO tarjetaDTO = new TarjetaDTO(1L, 1L, TipoCarta.EMOCIONAL, "pregunta", "texto", "cap1",2);

        SesionInicioDTO dto = new SesionInicioDTO();
        dto.setHayTarjetas(true);
        dto.setMensaje("Sesión iniciada correctamente.");
        dto.setSesionId("sesionCreada-123");
        dto.setTarjeta(tarjetaDTO);

        when(sesionServicio.iniciarSesion("usuarioTest"))
            .thenReturn(dto);

        mockMvc.perform(post("/api/v1/autenticacion/repaso/iniciar")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("usuarioTest")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sesionId")
                    .value("sesionCreada-123"));   
    }

    @Test
    void iniciarSesion_usuarioSinTarjetas_devuelve200() throws Exception{
        SesionInicioDTO dto = new SesionInicioDTO();
        dto.setHayTarjetas(false);
        dto.setMensaje("Sesión iniciada correctamente. No contiene tarjetas repasables.");
        dto.setSesionId("sesionId-123");
        dto.setTarjeta(null);

        when(sesionServicio.iniciarSesion("usuarioTest"))
            .thenReturn(dto);

        mockMvc.perform(post("/api/v1/autenticacion/repaso/iniciar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("usuarioTest")))
                        .andExpect(jsonPath("$.mensaje")
                                            .value("Sesión iniciada correctamente. No contiene tarjetas repasables."))
                        .andExpect(jsonPath("$.hayTarjetas")
                                            .value(false))
                        .andExpect(jsonPath("$.tarjeta").doesNotExist());
    }
    
    @Test
    void iniciarSesion_usuarioInexistente_devuelve404() throws Exception {
        when(sesionServicio.iniciarSesion("usuarioTest"))
            .thenThrow(new RecursoNoEncontrado("Usuario no encontrado."));

        mockMvc.perform(post("/api/v1/autenticacion/repaso/iniciar")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("usuarioTest")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje")
                    .value("Usuario no encontrado."));
    }

    @Test
    void responderTarjeta_devuelve200() throws Exception{
        RespuestaTarjetaDTO respuesta = new RespuestaTarjetaDTO();
        respuesta.setEstadoSesion(new EstadoSesionDTO(false, 1,2));
        respuesta.setFeedBack(new FeedBack(1L, 5, true));
        respuesta.setTarjeta(new TarjetaDTO(2L, 1L, TipoCarta.EMOCIONAL, "Pregunta","textoCarta", "cap1", 5));
        respuesta.setMensaje("Tarjetas correctamente devueltas.");

        when(sesionServicio.responderTarjeta("sesion123", 5))
            .thenReturn(respuesta);

        Cookie cookie = new Cookie("sesionId", "sesion123");

        mockMvc.perform(post("/api/v1/autenticacion/repaso/sesion/responder", "sesion123")
                        .cookie(cookie)
                        .content("5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("usuarioTest"))
                        )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.feedBack.calificacion")
                                    .value(5))
                        .andExpect(jsonPath("$.feedBack.tarjetaRespondidaId")
                                    .value(1L))
                        .andExpect(jsonPath("$.tarjeta.tarjetaId")
                                    .value(2L))
                        .andExpect(jsonPath("$.estadoSesion.sesionFinalizada")
                                    .value(false))
                        .andExpect(jsonPath("$.mensaje")
                                    .value("Tarjetas correctamente devueltas."));
    }

    @Test
    void responderTarjeta_sesionInexistente_devuelve404() throws Exception{
        when(sesionServicio.responderTarjeta("sesionInexistente", 4))
            .thenThrow(new RecursoNoEncontrado("No se encontró ninguna sesión."));

        Cookie cookie = new Cookie("sesionId", "sesionInexistente");
        
        mockMvc.perform(post("/api/v1/autenticacion/repaso/sesion/responder")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("usuarioTest"))
                        .content("4"))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.mensaje")
                                    .value("No se encontró ninguna sesión."));
    }

    @Test
    void responderTarjeta_sesionFinalizada_devuelve409() throws Exception{
        when(sesionServicio.responderTarjeta("sesionId", 4))
            .thenThrow(new SesionFinalizada("La sesión está finalizada."));

        Cookie cookie = new Cookie("sesionId", "sesionId");

        mockMvc.perform(post("/api/v1/autenticacion/repaso/sesion/responder")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("usuarioTest"))
                        .content("4"))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.mensaje")
                            .value("La sesión está finalizada."));       
    }

    @Test
    void responderTarjeta_calificacionInvalida_devuelve400() throws Exception{
        when(sesionServicio.responderTarjeta("sesionId", 7))
            .thenThrow(new DatosInvalidos("Calificación inválida."));

        Cookie cookie = new Cookie("sesionId", "sesionId");

        mockMvc.perform(post("/api/v1/autenticacion/repaso/sesion/responder")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("usuarioTest"))
                        .content("7"))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.mensaje")
                                    .value("Calificación inválida."));
    }
}