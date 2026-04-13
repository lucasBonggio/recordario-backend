package com.recordario.preguntas.asignacion.validacion;

import java.util.List;

import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.preguntas.Pregunta;

public class ValidarRedundancia implements ValidarPregunta{

    @Override
    public boolean valida(Pregunta pregunta, Carta carta, CapituloProcesado analisisCap, List<Pregunta> preguntasDelCapitulo) {
        if(pregunta.getTexto() == null) return false;
        for (Pregunta preg : preguntasDelCapitulo) {
            if(preg == null || preg.getTexto() == null) continue;
            if(sonEquivalentes(pregunta, preg)){
                return false;
            }
        }
        return true;
    }

    public boolean sonEquivalentes(Pregunta pregunta, Pregunta preguntaCap){
        String preg1 = normalizarParaComparacion(pregunta.getTexto());
        String preg2 = normalizarParaComparacion(preguntaCap.getTexto());
        
        System.out.println("ID IDEA1: " + pregunta.getIdNota());
        System.out.println("ID IDEA 2 : " + preguntaCap.getIdNota());


        if(pregunta.getIdNota() != preguntaCap.getIdNota()){
            return preg1.equals(preg2);
        } 
        return false;
    }

    public String normalizarParaComparacion(String texto) {
        if (texto == null) return "";
        
        return texto.toLowerCase()
                    .replaceAll("[¿?¡!.,;:\"'()\\[\\]{}«»—–\\-]", "") 
                    .replaceAll("\\s+", " ") 
                    .trim();
    }

}
