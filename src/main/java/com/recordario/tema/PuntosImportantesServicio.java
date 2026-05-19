package com.recordario.tema;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PuntosImportantesServicio {

    public String unirPuntosImportantes(List<String> puntosImportantes){
        String puntos = "";

        for (String punto : puntosImportantes) {
            if(tienePuntoFinal(punto)) puntos += punto;
            puntos += punto + ". ";
        }

        System.out.println(puntos);
        return puntos;
    };

    public boolean tienePuntoFinal(String oracion){
        return oracion.endsWith(".") || oracion.endsWith(". ");
    }
}
