package com.recordario.preguntas.asignacion;

import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;

public interface ReglaIntencion {
    boolean aplica(Pregunta pregunta);
    boolean contieneSeñales(Pregunta pregunta);
    Tipo detectarIntencion();
    int detectarPrioridad();    
}
