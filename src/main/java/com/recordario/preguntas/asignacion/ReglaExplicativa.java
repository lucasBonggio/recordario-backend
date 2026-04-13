package com.recordario.preguntas.asignacion;

import java.util.List;

import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;

import lombok.Data;

@Data
public class ReglaExplicativa implements ReglaIntencion{
    List<String> señales = List.of(
        "explicá",
        "explicá cómo",
        "explicá por qué",
        "¿cómo funciona",
        "¿qué provoca",
        "describe el proceso"
    );

    @Override
    public boolean aplica(Pregunta pregunta) {
        return contieneSeñales(pregunta);
    }

    @Override
    public boolean contieneSeñales(Pregunta pregunta){
        for (String señal : this.señales) {
            String textoMinusculas = pregunta.getTexto().toLowerCase();
            if(textoMinusculas.startsWith(señal) || textoMinusculas.contains(señal)) return true;
        }
        return false;
    }

    @Override
    public Tipo detectarIntencion() {
        return Tipo.EXPLICACION;
    }

    @Override
    public int detectarPrioridad() {
        return 80;
    }
}
