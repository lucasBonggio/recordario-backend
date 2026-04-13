package com.recordario.preguntas.creadores;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;
import com.recordario.preguntas.Pregunta;

public interface FabricadorPreguntas {
    boolean aplicaA(TipoCarta tipo);
    Pregunta generar(Carta carta);
}
