package com.recordario.preguntas.creadores;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.Tipo;
import com.recordario.compartido.enums.TipoCarta;
import com.recordario.preguntas.Pregunta;

public class FabricaPreguntaAtmosfera implements FabricadorPreguntas {

    @Override
    public boolean aplicaA(TipoCarta tipoCarta) {
        return tipoCarta == TipoCarta.ATMOSFERA;
    }

    @Override
    public Pregunta generar(Carta carta) {
        Pregunta pregunta = new Pregunta();

        String emocion = carta.getEmocion();
        if (emocion == null || emocion.isBlank()) {
            emocion = "la sensación general";
        }

        String texto = "¿Cómo contribuye el ambiente descrito a reforzar "
                + emocion
                + "?";

        pregunta.setIdNota(carta.getIdNota());
        pregunta.setTipo(Tipo.FACTUAL);
        pregunta.setTexto(texto);
        return pregunta;
    }
}

