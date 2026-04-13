package com.recordario;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordario.cartas.Carta;
import com.recordario.excepciones.tipos.DatosInvalidos;
import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.excepciones.tipos.SesionFinalizada;
import com.recordario.repaso.GestorSesionesRepaso;
import com.recordario.repaso.OrdenarPrioridad;
import com.recordario.repaso.SesionRepaso;
import com.recordario.repaso.SesionRepasoServicio;
import com.recordario.repaso.dto.RespuestaTarjetaDTO;
import com.recordario.repaso.dto.SesionInicioDTO;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaServicio;
import com.recordario.tarjetas.dto.TarjetaRespuesta;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioServicio;

@ExtendWith(MockitoExtension.class)
public class TestSesionRepasoServicio {

    @InjectMocks
    private SesionRepasoServicio sesionServicio;

    @Mock
    private GestorSesionesRepaso gestorSesion;

    @Mock
    private TarjetaServicio tarjetaServicio;

    @Mock
    private UsuarioServicio usuarioServicio;

    @Mock
    private OrdenarPrioridad ordenarPrioridad;

    @Test
    void responderTarjeta_avanzarSesion_yDevuelveLaSiguiente(){
        String sesionId = "sesionId-123";

        Carta carta = new Carta();
        carta.setId(1L);

        Tarjeta tarjeta1 = new Tarjeta();
        tarjeta1.setId(1L);
        tarjeta1.setCarta(carta);

        Tarjeta tarjeta2 = new Tarjeta();
        tarjeta2.setId(2L);
        tarjeta2.setCarta(carta);

        List<Tarjeta> tarjetas = List.of(tarjeta1, tarjeta2);

        SesionRepaso sesion = new SesionRepaso("usuarioTest", tarjetas);

        when(gestorSesion.obtenerSesion(sesionId))
            .thenReturn(sesion);

        TarjetaRespuesta tarjetaRespuesta = new TarjetaRespuesta();
        tarjetaRespuesta.setTarjetaId(1L);
        tarjetaRespuesta.setCalificacion(5);

        when(tarjetaServicio.responderTarjeta(eq(1L), eq(5), eq(LocalDate.now())))
            .thenReturn(tarjetaRespuesta);

        RespuestaTarjetaDTO respuesta = sesionServicio.responderTarjeta(sesionId, 5);

        assertEquals(1L, sesion.getIndiceActual());
        assertNotNull(respuesta.getTarjeta());
        assertEquals(1L, respuesta.getFeedBack().getTarjetaRespondidaId());
        assertEquals(2L, respuesta.getTarjeta().tarjetaId());
    }

    @Test
    void responderTarjeta_avanzarSesion_finalizarSesion(){
        String sesionId = "sesionId-567";

        Carta carta = new Carta();
        
        Tarjeta tarjeta1 = new Tarjeta();
        tarjeta1.setId(1L);
        tarjeta1.setCarta(carta);

        List<Tarjeta> tarjetas = List.of(tarjeta1);

        SesionRepaso sesion = new SesionRepaso("usuarioTest", tarjetas);

        when(gestorSesion.obtenerSesion(sesionId))
            .thenReturn(sesion);

        TarjetaRespuesta tarjetaRespuesta = new TarjetaRespuesta();
        tarjetaRespuesta.setTarjetaId(1L);
        tarjetaRespuesta.setCalificacion(5);

        when(tarjetaServicio.responderTarjeta(eq(1L), eq(5), eq(LocalDate.now())))
            .thenReturn(tarjetaRespuesta);

        RespuestaTarjetaDTO respuesta = sesionServicio.responderTarjeta(sesionId, 5);

        assertEquals(1L, sesion.getIndiceActual());
        assertNull(respuesta.getTarjeta());
        assertEquals(1L, respuesta.getFeedBack().getTarjetaRespondidaId());
        assertEquals("No se encontraron mas tarjetas. Sesión finalizada.", respuesta.getMensaje());
    }

    @Test
    void responderTarjeta_respuestaNula_lanzaExcepcionYNoAvanza(){
        String sesionId = "sesion123123";
        
        Carta carta = new Carta();

        Tarjeta tarjeta1 = new Tarjeta();
        tarjeta1.setId(1L);
        tarjeta1.setCarta(carta);

        List<Tarjeta> tarjetas = List.of(tarjeta1);

        SesionRepaso sesion = new SesionRepaso("usuarioTest", tarjetas);
        sesion.setIndiceActual(0);

        when(gestorSesion.obtenerSesion(sesionId))
            .thenReturn(sesion);
        
        when(tarjetaServicio.responderTarjeta(eq(1L), eq(6), eq(LocalDate.now())))
            .thenReturn(null);

        DatosInvalidos excepcion = assertThrows(DatosInvalidos.class, 
            () -> {
                sesionServicio.responderTarjeta(sesionId, 6);
        });

        assertEquals("Respuesta inválida. Intentelo nuevamente.", excepcion.getMessage());
        assertEquals(0, sesion.getIndiceActual());        
    }
    
    @Test
    void responderTarjeta_sesionInexistente(){
        String sesionId = "noExiste";

        when(gestorSesion.obtenerSesion(sesionId)).thenThrow(new RecursoNoEncontrado("No se encontró ninguna sesión."));

        RecursoNoEncontrado excepcion = assertThrows(RecursoNoEncontrado.class, 
            () -> {
                sesionServicio.responderTarjeta(sesionId, 5);
            });

        assertEquals("No se encontró ninguna sesión.", excepcion.getMessage());
    }

    @Test
    void responderTarjeta_sesionFinalizada(){
        String sesionId = "sesionFinalizada";
        SesionRepaso sesion = new SesionRepaso("usuarioTest", List.of());
        sesion.setFinalizada(true);

        when(gestorSesion.obtenerSesion(sesionId))
            .thenReturn(sesion);

        SesionFinalizada excepcion = assertThrows(SesionFinalizada.class, 
            () -> {
                sesionServicio.responderTarjeta(sesionId, 5);
            }
        );

        assertEquals("La sesión está finalizada.", excepcion.getMessage());
    }


    @Test
    void iniciarSesion_usuarioSinTarjetas(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");
        
        when(usuarioServicio.obtenerUsuario("usuarioTest"))
            .thenReturn(usuario);

        when(sesionServicio.ordenarTarjetaPorPrioridad(List.of(), ordenarPrioridad))
            .thenReturn(List.of());

        when(gestorSesion.crearSesion(any()))   
            .thenReturn("sesionCreada-123");

        SesionInicioDTO inicioDTO = sesionServicio.iniciarSesion("usuarioTest");
        
        
        assertNotNull(inicioDTO);
        assertEquals("Sesión iniciada correctamente. No contiene tarjetas repasables.", inicioDTO.getMensaje());
        assertEquals(null, inicioDTO.getTarjeta());
    }

    @Test
    void iniciarSesion_contieneTarjetas() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioTest");

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(1L);
        tarjeta.setProximaRevision(LocalDate.now().minusDays(6));
        tarjeta.setCarta(new Carta());
        tarjeta.setUsuario(usuario);

        List<Tarjeta> tarjetas = List.of(tarjeta);

        when(usuarioServicio.obtenerUsuario("usuarioTest"))
            .thenReturn(usuario);

        when(sesionServicio.ordenarTarjetaPorPrioridad(tarjetas, ordenarPrioridad))
            .thenReturn(tarjetas);

        when(sesionServicio.obtener15TarjetasRepasables(usuario))
            .thenReturn(tarjetas);
        
        when(gestorSesion.crearSesion(any()))
            .thenReturn("sesionCreada-123");

        SesionInicioDTO respuesta = sesionServicio.iniciarSesion("usuarioTest");
        
        assertNotNull(respuesta);
        assertTrue(respuesta.isHayTarjetas());
        assertEquals("sesionCreada-123", respuesta.getSesionId());
        assertEquals(1L, respuesta.getTarjeta().tarjetaId());
    }
}
