package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;

public class AnalizadorAtmosfera {
    public static final Map<String, TipoCarta> atmosferas = Map.of(
        "oscuridad", TipoCarta.OSCURIDAD,
        "sombras", TipoCarta.OSCURIDAD,
        "misterio", TipoCarta.OSCURIDAD,
        "caos", TipoCarta.OSCURIDAD,
        "calma", TipoCarta.EMOCIONAL
    );

    public List<Carta> analizar(String texto, Long idNota){
        List<Carta> cartas = new ArrayList<>();
        String enMinusculas = texto.toLowerCase();

        atmosferas.forEach((atm, tipo) -> {

            int count = contarOcurrencias(enMinusculas, atm);

            if(count > 0){

                cartas.add(generarCartaAtmosfera(texto, idNota, tipo, count, atm));
            }
        });

        return cartas;
    }

    private Carta generarCartaAtmosfera(String texto, Long idNota, TipoCarta tipo, int count, String atm){
        Carta c = new Carta();
        c.setIdNota(idNota);
        c.setTipoCarta(tipo);
        c.setEmocion(atm);
        c.setDescripcion(texto);
        c.setIntensidad(count);

        return c;
    }
    

    public int contarOcurrencias(String texto, String palabra){
        int count= 0;
        int index= 0;

        while((index = texto.indexOf(palabra, index)) != -1){
            count++;
            index += palabra.length();
        }
        return count;
    }
}
