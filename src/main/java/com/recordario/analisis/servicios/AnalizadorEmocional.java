package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;

public class AnalizadorEmocional {
    public static final Map<String, TipoCarta> emociones = Map.of(
        "miedo", TipoCarta.EMOCIONAL,
        "terror", TipoCarta.EMOCIONAL,
        "alegría", TipoCarta.EMOCIONAL,
        "tristeza", TipoCarta.EMOCIONAL,
        "enojo", TipoCarta.EMOCIONAL
    );

    public List<Carta> analizar(String texto, Long idNota){
        List<Carta> cartas = new ArrayList<>();

        String enMinusculas = texto.toLowerCase();

        emociones.forEach((palabra, tipo) -> {
            int contador = contarVeces(enMinusculas, palabra);
            if(contador > 0){
                cartas.add(crearCartaEmocional(texto, palabra, idNota, contador));
            }
        });

        return cartas;
    }

    private Carta crearCartaEmocional(String texto, String emocion, Long idNota, int intensidad){
        Carta c = new Carta();
        c.setIdNota(idNota);
        c.setTipoCarta(TipoCarta.EMOCIONAL);
        c.setEmocion(emocion);
        c.setDescripcion(texto);
        c.setIntensidad(intensidad);

        return c;
    }

    public int contarVeces(String texto, String patron){
        int contador = 0;
        int idx = texto.indexOf(patron);

        while(idx != -1){
            contador++;
            idx = texto.indexOf(patron, idx +1);
        }

        return contador;
    } 
}
