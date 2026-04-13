package com.recordario.preguntas.asignacion;

import java.util.List;

import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;

import lombok.Data;
@Data
public class ReglaFactual implements ReglaIntencion{
    List<String> señales = List.of(
        "¿qué es",
        "¿cuál es",
        "¿cuándo",
        "¿dónde",
        "¿quién",
        "define",
        "según el texto"
    );

    @Override
    public boolean aplica(Pregunta pregunta) {
        return contieneSeñales(pregunta);
    }

    @Override
    public boolean contieneSeñales(Pregunta pregunta){
        for (String señal : this.señales) {
            String textoMinusculas = pregunta.getTexto().toLowerCase();
            if(textoMinusculas.startsWith(señal)) return true;
        }
        return false;
    }

    @Override
    public Tipo detectarIntencion() {
        return Tipo.FACTUAL;
    }

    @Override
    public int detectarPrioridad() {
        return 20;
    }
}
