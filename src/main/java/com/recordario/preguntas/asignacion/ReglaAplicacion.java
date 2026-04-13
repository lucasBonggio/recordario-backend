package com.recordario.preguntas.asignacion;

import java.util.List;

import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;

public class ReglaAplicacion implements ReglaIntencion {
    List<String> señales = List.of(
        "¿cómo aplicarías",
        "¿cómo usarías",
        "¿qué harías",
        "¿en qué situación usarías",
        "¿cómo resolverías",
        "¿qué decisión tomarías"
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
        return Tipo.APLICACION;
    }

    @Override
    public int detectarPrioridad() {
        return 100;
    }
}
