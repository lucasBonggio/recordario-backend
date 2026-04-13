package com.recordario.preguntas.creadores;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.Tipo;
import com.recordario.compartido.enums.TipoCarta;
import com.recordario.preguntas.Pregunta;

public class FabricaPreguntaEmocional implements FabricadorPreguntas {

    @Override
    public boolean aplicaA(TipoCarta tipoCarta) {
        return tipoCarta == TipoCarta.EMOCIONAL;
    }

    @Override
    public Pregunta generar(Carta carta) {
        Pregunta pregunta = new Pregunta();

        String emocion = carta.getEmocion();
        if (emocion == null || emocion.isBlank()) {
            emocion = "esa emoción";
        }

        String texto = "¿Qué es lo que lleva al personaje a sentir "
                + emocion
                + " en este momento?";

        pregunta.setIdNota(carta.getIdNota());
        pregunta.setTipo(Tipo.FACTUAL);
        pregunta.setTexto(texto);
        return pregunta;
    }
}
