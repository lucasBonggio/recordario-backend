package com.recordario.preguntas.creadores;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.Tipo;
import com.recordario.compartido.enums.TipoCarta;
import com.recordario.preguntas.Pregunta;
public class FabricaPreguntaTematica implements FabricadorPreguntas {

    @Override
    public boolean aplicaA(TipoCarta tipoCarta) {
        return tipoCarta == TipoCarta.TEMATICA;
    }

    @Override
    public Pregunta generar(Carta carta) {
        Pregunta pregunta = new Pregunta();

        String concepto = carta.getDescripcion();
        if (concepto == null || concepto.isBlank()) {
            concepto = "este concepto";
        }

        String texto = "¿Cómo se relaciona "
                + concepto
                + " con el tema principal del texto?";

        pregunta.setIdNota(carta.getIdNota());
        pregunta.setTipo(Tipo.FACTUAL);
        pregunta.setTexto(texto);
        return pregunta;
    }
}
