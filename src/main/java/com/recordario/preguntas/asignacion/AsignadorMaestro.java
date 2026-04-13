package com.recordario.preguntas.asignacion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.analisis.modelos.Coincidencia;
import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;


@Service
public class AsignadorMaestro {
    private final List<ReglaIntencion> reglas = List.of(
        new ReglaAplicacion(),
        new ReglaConceptual(),
        new ReglaExplicativa(),
        new ReglaRecuerdo(),
        new ReglaFactual()
    );

    public Pregunta asignarIntencion(Pregunta pregunta){
        if(pregunta == null) return null;
        List<Coincidencia> coincidencias = new ArrayList<>();

        for (ReglaIntencion regla : reglas) {
            if(regla.aplica(pregunta)){
                
                Coincidencia coincidencia = new Coincidencia();
                coincidencia.setIntencion(regla.detectarIntencion());
                coincidencia.setPrioridad(regla.detectarPrioridad());
                
                coincidencias.add(coincidencia);
            }
        }
        int prioridad1 = coincidencias.get(0).getPrioridad();
        Tipo tipo = coincidencias.get(0).getIntencion();
        
        for (int i = 1; i < coincidencias.size(); i++) {
            if(prioridad1 < coincidencias.get(i).getPrioridad()){
                prioridad1 = coincidencias.get(i).getPrioridad();
                tipo = coincidencias.get(i).getIntencion();
            }
        }
        pregunta.setTipo(tipo);

        return pregunta;
    }
}