package com.recordario.preguntas.asignacion.validacion;

import java.util.List;

import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.preguntas.Pregunta;

public class ValidarLongitud implements ValidarPregunta {

    @Override
    public boolean valida(Pregunta pregunta, Carta carta, CapituloProcesado analisisCap, List<Pregunta> preguntasDelCapitulo) {
        if(pregunta == null) return false;

        int longitud = pregunta.getTexto().length();
        return longitud >= 7;
    }
}
