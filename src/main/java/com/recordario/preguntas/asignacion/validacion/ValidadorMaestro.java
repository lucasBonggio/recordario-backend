package com.recordario.preguntas.asignacion.validacion;

import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.preguntas.Pregunta;

@Service
public class ValidadorMaestro {
    private final List<ValidarPregunta> validadores;

    public ValidadorMaestro() {
        this.validadores = List.of(
                new ValidarFormato(),
                new ValidarLongitud(),
                new ValidarCoherencia(),
                new ValidarRedundancia()
        );
    }

    public Pregunta validarPregunta(Pregunta pregunta, Carta carta, CapituloProcesado analisisCap, List<Pregunta> preguntasDelCapitulo){
        if(pregunta == null) return null;

        for(ValidarPregunta validador : validadores){
            boolean aprueba = validador.valida(pregunta, carta, analisisCap, preguntasDelCapitulo);

            if(!aprueba){
                return null;
            }
        }
        pregunta.setValidado(true);
        return pregunta;
    };
}
