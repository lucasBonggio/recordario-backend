package com.recordario;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.recordario.cartas.Carta;
import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.excepciones.tipos.SesionFinalizada;
import com.recordario.repaso.SesionRepasoEntidad;
import com.recordario.repaso.SesionRepasoRepositorio;
import com.recordario.repaso.SesionRepasoServicio;
import com.recordario.repaso.dto.RespuestaTarjetaDTO;
import com.recordario.repaso.dto.SesionInicioDTO;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaRepositorio;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioRepositorio;
import com.recordario.usuarios.UsuarioServicio;
import com.recordario.usuarios.dto.ProgresoDTO;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SesionRepasoTestIntegracion {
    @Autowired
    private SesionRepasoServicio sesionRepasoServicio;

    @Autowired
    private SesionRepasoRepositorio sesionRepasoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TarjetaRepositorio tarjetaRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Test
    void crearSesion_yRecuperarla(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuarioRepositorio.save(usuario);
        
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCarta(new Carta());
        tarjeta.setUsuario(usuario);
        tarjeta.setProximaRevision(LocalDate.now().minusDays(3));
        tarjetaRepositorio.save(tarjeta);

        SesionInicioDTO inicio = sesionRepasoServicio.iniciarSesion(usuario.getNombreUsuario());
        
        assertNotNull(inicio);

        SesionRepasoEntidad entidad = sesionRepasoRepositorio
            .findById(inicio.getSesionId())
            .orElseThrow();

        assertEquals(usuario.getNombreUsuario(), entidad.getNombreUsuario());
        assertFalse(entidad.isFinalizada());
    }

    @Test
    void iniciarSesion_sinTarjetas_recuperaSesionFinalizada(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuarioRepositorio.save(usuario);

        SesionInicioDTO inicio = sesionRepasoServicio.iniciarSesion(usuario.getNombreUsuario());

        assertNotNull(inicio);

        SesionRepasoEntidad entidad = sesionRepasoRepositorio
            .findById(inicio.getSesionId())
            .orElseThrow();
        
        assertEquals(usuario.getNombreUsuario(), entidad.getNombreUsuario());
        assertEquals("Sesión iniciada correctamente. No contiene tarjetas repasables.", inicio.getMensaje());
        assertNull(inicio.getTarjeta());
        assertTrue(entidad.isFinalizada());
    }

    @Test
    void flujoCompleto_iniciarSesionResponderTarjetasYMedirProgreso_todoOk(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuarioRepositorio.save(usuario);

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCarta(new Carta());
        tarjeta.setUsuario(usuario);
        tarjeta.setProximaRevision(LocalDate.now().minusDays(3));
        tarjetaRepositorio.save(tarjeta);

        Tarjeta tarjeta2 = new Tarjeta();
        tarjeta2.setCarta(new Carta());
        tarjeta2.setUsuario(usuario);
        tarjeta2.setProximaRevision(LocalDate.now().minusDays(5));
        tarjetaRepositorio.save(tarjeta2);

        Tarjeta tarjeta3 = new Tarjeta();
        tarjeta3.setCarta(new Carta());
        tarjeta3.setUsuario(usuario);
        tarjeta3.setProximaRevision(LocalDate.now().minusDays(16));
        tarjetaRepositorio.save(tarjeta3);

        SesionInicioDTO inicio = sesionRepasoServicio.iniciarSesion(usuario.getNombreUsuario());

        assertNotNull(inicio);

        //Respondemos la primer tarjeta
        RespuestaTarjetaDTO respuesta = sesionRepasoServicio
            .responderTarjeta(inicio.getSesionId(), 4);
        
        assertTrue(respuesta.getFeedBack().isFueCorrecta());
        assertEquals(1L, respuesta.getFeedBack().getTarjetaRespondidaId());
        assertEquals("Tarjetas correctamente devueltas.", respuesta.getMensaje());     

        //Respondemos la segunda pregunta.
        RespuestaTarjetaDTO respuesta2 = sesionRepasoServicio
            .responderTarjeta(inicio.getSesionId(), 5);

        assertTrue(respuesta2.getFeedBack().isFueCorrecta());
        assertEquals(2L, respuesta2.getFeedBack().getTarjetaRespondidaId());

        //Obtenemos el progreso
        ProgresoDTO progreso = usuarioServicio.obtenerProgreso(usuario.getNombreUsuario());
        
        assertNotNull(progreso);

        assertEquals(3, progreso.tarjetasTotales());
        assertEquals(2, progreso.tarjetasRepasadasHoy());
        assertEquals(1, progreso.tarjetasPendientes());
    }

    @Test
    void responderTarjeta_sesionInexistente_devuelveExcepcion(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuarioRepositorio.save(usuario);

        RecursoNoEncontrado excepcion = assertThrows(RecursoNoEncontrado.class,
            () -> {
                sesionRepasoServicio
                    .responderTarjeta("sesionInexistente123", 4);
        });
        
        assertEquals("Sesión no encontrada.", excepcion.getMessage()); 
    }

    @Test
    void responderTarjeta_sesionFinalizada_devuelveExcepcion(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuarioRepositorio.save(usuario);

        SesionInicioDTO inicio = sesionRepasoServicio
                                    .iniciarSesion(usuario.getNombreUsuario());
        
        assertNotNull(inicio);                            
        
        SesionFinalizada excepcion = assertThrows(SesionFinalizada.class, 
            () -> {
                sesionRepasoServicio
                    .responderTarjeta(inicio.getSesionId(), 4);
        });
        
        assertEquals("La sesión está finalizada.", excepcion.getMessage());
    }
}
