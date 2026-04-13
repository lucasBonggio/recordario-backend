package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;

public class AnalizadorTematico {
    private static final Map<String, TipoCarta> temas = Map.of(
        "identidad", TipoCarta.EMOCIONAL,
        "responsabilidad", TipoCarta.ATMOSFERA,
        "conflicto moral", TipoCarta.ATMOSFERA,
        "poder", TipoCarta.EMOCIONAL,
        "libertad", TipoCarta.REFLEXION
    );

    public List<Carta> analizar(String texto, Long idNota){
        List<Carta> cartas = new ArrayList<>();
        
        String enMinusculas = texto.toLowerCase();

        for(String tema: temas.keySet()){
            if(enMinusculas.contains(tema)){

                cartas.add(generarCartaTematica(texto, tema, idNota));
            }
        }
    
        return cartas;
    }

    private Carta generarCartaTematica(String texto, String tema, Long idNota){
        Carta c = new Carta();
        c.setIdNota(idNota);
        c.setTipoCarta(temas.get(tema));
        c.setEmocion(tema);
        c.setDescripcion(texto);
        c.setIntensidad(3);
        
        return c;
    }
}
