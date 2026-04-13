package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.analisis.dto.AnalisisLibro;
import com.recordario.analisis.dto.AnalisisRespuestaDTO;
import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.libro.Libro;
import com.recordario.libro.LibroServicio;
import com.recordario.preguntas.Pregunta;
import com.recordario.preguntas.asignacion.AsignadorMaestro;
import com.recordario.preguntas.asignacion.validacion.ValidadorMaestro;
import com.recordario.preguntas.creadores.GeneradorPreguntas;
import com.recordario.tarjetas.TarjetaServicio;
import com.recordario.tarjetas.dto.DatosTarjeta;

@Service
public class AnalisisLibroServicio {

    private final LibroServicio libroServicio;
    private final GeneradorPreguntas generadorPreguntas;
    private final ValidadorMaestro validadorMaestro;
    private final AsignadorMaestro asignadorMaestro;
    private final TarjetaServicio tarjetaServicio;

    public AnalisisLibroServicio(LibroServicio libroServicio, GeneradorPreguntas generadorPreguntas, ValidadorMaestro validadorMaestro, AsignadorMaestro asignadorMaestro, TarjetaServicio tarjetaServicio){
        this.libroServicio = libroServicio;
        this.generadorPreguntas = generadorPreguntas;
        this.validadorMaestro = validadorMaestro;
        this.asignadorMaestro = asignadorMaestro;
        this.tarjetaServicio = tarjetaServicio;
    }

    public AnalisisRespuestaDTO analizarLibro(Libro libro, String nombreUsuario){
        AnalisisLibro respuesta = libroServicio.analizar(libro);
        int tarjetasCreadas = 0;

        for (CapituloProcesado analisisCap : respuesta.capitulos()) {
            List<Pregunta> preguntasValidadas = new ArrayList<>();

            for (Carta carta : analisisCap.cartas()) {
                Pregunta pregunta = generadorPreguntas.generarPregunta(carta);

                if (pregunta == null) continue;
                
                Pregunta preguntaValidada =
                validadorMaestro.validarPregunta(
                    pregunta,
                    carta,
                    analisisCap,
                    preguntasValidadas
                );
                
                if (preguntaValidada != null) {
                    asignadorMaestro.asignarIntencion(preguntaValidada);

                    DatosTarjeta datosTarjeta = new DatosTarjeta(
                        nombreUsuario,
                        carta,
                        preguntaValidada.getTexto(),
                        analisisCap.titulo()
                    );

                    tarjetaServicio.crearTarjeta(datosTarjeta);
                    tarjetasCreadas++;

                    preguntasValidadas.add(preguntaValidada);
                }
            }
        }

        return new AnalisisRespuestaDTO(tarjetasCreadas);
    }
}
