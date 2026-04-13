package com.recordario.preguntas.asignacion.validacion;

import java.util.List;

import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.preguntas.Pregunta;

public interface ValidarPregunta {
    boolean valida(Pregunta pregunta, Carta carta, CapituloProcesado analisisCap, List<Pregunta> preguntasDelCapitulo);
}
