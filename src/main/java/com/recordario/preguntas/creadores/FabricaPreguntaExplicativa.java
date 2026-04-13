package com.recordario.preguntas.creadores;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.Tipo;
import com.recordario.compartido.enums.TipoCarta;
import com.recordario.preguntas.Pregunta;

public class FabricaPreguntaExplicativa implements FabricadorPreguntas {

    @Override
    public boolean aplicaA(TipoCarta tipoCarta) {
        return tipoCarta == TipoCarta.EXPLICATIVA;
    }

    @Override
    public Pregunta generar(Carta carta) {
        Pregunta pregunta = new Pregunta();

        String emocion = carta.getEmocion();
        if(emocion == null || emocion.isBlank()){
            emocion = "este tema";
        }
        pregunta.setTexto(
            "Explicá con tus palabras " + emocion 
        );
        pregunta.setIdNota(carta.getIdNota());
        pregunta.setTipo(Tipo.EXPLICACION);
        return pregunta;
    }
}
