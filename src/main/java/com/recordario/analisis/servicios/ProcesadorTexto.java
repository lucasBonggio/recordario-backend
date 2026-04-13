package com.recordario.analisis.servicios;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ProcesadorTexto {
    public static final Set<String> STOPWORDS = Set.of(
    "el","la","los","las","un","una","unos","unas",
        "de","del","al","a","que","y","o","en","es","su",
        "por","para","con","se","lo","como","muy","pero"
    );

    public List<String> extraerFrases(String texto){
        if(texto.isBlank()) return List.of();

            return Arrays.stream(texto.split("[\\.;:\\n]"))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList();
    }

    public int puntuarFrase(String frase){
        String[] palabras =  frase.toLowerCase()
                            .replaceAll("[^a-záéíóú]", "")
                            .split("\\s+");

        int puntuacion = 0;
        for(String p: palabras){
            if(!STOPWORDS.contains(p) && p.length() > 2){
                puntuacion++;
            }
        }
        return puntuacion;
    }
}
