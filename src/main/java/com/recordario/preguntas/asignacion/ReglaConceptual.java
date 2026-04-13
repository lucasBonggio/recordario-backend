package com.recordario.preguntas.asignacion;

import java.util.List;

import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;

import lombok.Data;


@Data
public class ReglaConceptual implements ReglaIntencion{
    List<String> señales = List.of(
        "¿por qué",
        "¿cómo se relaciona",
        "compara",
        "diferencia entre",
        "explica la relacion",
        "analiza"
    );

    @Override
    public boolean aplica(Pregunta pregunta) {
        return contieneSeñales(pregunta);
    }

    @Override
    public boolean contieneSeñales(Pregunta pregunta){
        for (String señal : this.señales) {
            String textoMinusculas = pregunta.getTexto().toLowerCase();
            if(textoMinusculas.contains(señal)) return true;
        }

        return false;
    }

    @Override
    public Tipo detectarIntencion() {
        return Tipo.CONCEPTUAL;
    }

    @Override
    public int detectarPrioridad() {
        return 50;
    }



}
