package com.recordario;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.repaso.sm2.Sm2Servicio;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaRepositorio;
import com.recordario.tarjetas.TarjetaServicio;
import com.recordario.tarjetas.dto.TarjetaRespuesta;
import com.recordario.usuarios.UsuarioServicio;

@Tag("tarjeta_testeos_servicio")
@ExtendWith(MockitoExtension.class)
public class TarjetaServicioTests {

    @InjectMocks
    private TarjetaServicio tarjetaServicio;

    @Mock
    private TarjetaRepositorio tarjetaRepositorio;

    @Mock
    private UsuarioServicio usuarioServicio;

    @BeforeEach
    void setUp(){
        tarjetaServicio = new TarjetaServicio(tarjetaRepositorio, 
            new Sm2Servicio(), usuarioServicio);
    }

    @Test
    void responderTarjeta_calificacionBaja_reiniciaProgreso(){
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(1L);
        tarjeta.setRepeticiones(3);
        tarjeta.setIntervalo(10);
        tarjeta.setFactorFacilidad(2.5);
        tarjeta.setProximaRevision(LocalDate.now().plusDays(4));

        when(tarjetaRepositorio.findById(1L))
            .thenReturn(Optional.of(tarjeta));

        TarjetaRespuesta respuesta = tarjetaServicio.responderTarjeta(1L, 2, LocalDate.now());

        assertNotNull(respuesta);      
        assertEquals(0, tarjeta.getRepeticiones());
        assertEquals(1, tarjeta.getIntervalo());
        assertTrue(tarjeta.getFactorFacilidad() < 2.5);
        assertTrue(tarjeta.getProximaRevision().isAfter(LocalDate.now()));        
    }

    @Test
    void responderTarjeta_calificacionMedia(){
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(1L);
        tarjeta.setRepeticiones(3);
        tarjeta.setIntervalo(10);
        tarjeta.setFactorFacilidad(2.5);
        tarjeta.setProximaRevision(LocalDate.now().plusDays(4));

        when(tarjetaRepositorio.findById(1L))
            .thenReturn(Optional.of(tarjeta));

        int intervaloAnterior = tarjeta.getIntervalo();

        TarjetaRespuesta respuesta = tarjetaServicio.responderTarjeta(1L, 3, LocalDate.now());

        assertNotNull(respuesta);
        assertEquals(4, tarjeta.getRepeticiones());
        assertTrue(tarjeta.getIntervalo() > intervaloAnterior);
        assertTrue(tarjeta.getFactorFacilidad() <= 2.5);
        assertTrue(tarjeta.getProximaRevision().isAfter(LocalDate.now()));        
    }

    @Test
    void responderTarjeta_calificacionAlta(){
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setId(1L);
        tarjeta.setRepeticiones(3);
        tarjeta.setIntervalo(10);
        tarjeta.setFactorFacilidad(2.5);
        tarjeta.setProximaRevision(LocalDate.now().plusDays(3));

        when(tarjetaRepositorio.findById(1L))
            .thenReturn(Optional.of(tarjeta));
        
            int intervaloAnterior = tarjeta.getIntervalo();
        double factorFacilidadAnterior = tarjeta.getFactorFacilidad();
        
        TarjetaRespuesta respuesta = tarjetaServicio.responderTarjeta(1L,5, LocalDate.now());
        
        assertNotNull(respuesta);
        assertEquals(4, tarjeta.getRepeticiones());
        assertTrue(tarjeta.getIntervalo() > intervaloAnterior);
        assertTrue(tarjeta.getFactorFacilidad() > factorFacilidadAnterior);
        assertTrue(tarjeta.getProximaRevision().isAfter(LocalDate.now()));
    }

    @Tag("no_existe_la_tarjeta_y_lanza_excepcion")
    @Test
    void responderTarjeta_tarjetaInexistente_lanzaExcepcion(){
        when(tarjetaRepositorio.findById(1l))
            .thenReturn(Optional.empty());

        RecursoNoEncontrado excepcion = assertThrows(
            RecursoNoEncontrado.class,
            () -> {
                tarjetaServicio.responderTarjeta(1L, 4, LocalDate.now());
            });

        assertEquals("Tarjeta no encontrada.", excepcion.getMessage());
    }
}
