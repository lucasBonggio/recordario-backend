package com.recordario.preguntas.asignacion.validacion;

import java.util.List;

import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.preguntas.Pregunta;

public class ValidarFormato implements ValidarPregunta {

    @Override
    public boolean valida(Pregunta pregunta, Carta carta, CapituloProcesado analisisCap, List<Pregunta> preguntasDelCapitulo) {
        return !(pregunta.getTexto().isEmpty() || pregunta.getTexto() == null);
    }
}
