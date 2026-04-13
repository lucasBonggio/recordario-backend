package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;

public class AnalizadorExplicativo {

    public List<Carta> analizar(String texto, Long idNota) {
        List<Carta> cartas = new ArrayList<>();

        if (!esTextoExplicativo(texto)) {
            return cartas;
        }

        cartas.add(generarCarta(texto, idNota));
        return cartas;
    }

    private Carta generarCarta(String texto, Long idNota){
        Carta carta = new Carta();
        carta.setIdNota(idNota);
        carta.setTipoCarta(TipoCarta.EXPLICATIVA);
        carta.setDescripcion(limpiarTexto(texto));
        carta.setIntensidad(calcularIntensidad(texto));

        return carta;
    }

    private boolean esTextoExplicativo(String texto) {
        if (texto == null) return false;

        String t = texto.toLowerCase().trim();

        return cumpleLongitudMinima(t)
            && noEsPregunta(t)
            && noEsEmocional(t)
            && noEsNarrativo(t)
            && contieneEstructuraExplicativa(t);
    }

    private boolean cumpleLongitudMinima(String texto) {
        return texto.length() >= 25 && texto.split(" ").length >= 4;
    }

    private boolean noEsPregunta(String texto) {
        return !texto.endsWith("?") && !texto.startsWith("¿");
    }

    private boolean noEsEmocional(String texto) {
        return !texto.matches(".*(me sentí|me siento|emocion|triste|feliz|miedo|enojo).*");
    }

    private boolean noEsNarrativo(String texto) {
        return !texto.matches(".*(yo |me |mi |cuando |recuerdo ).*");
    }

    private boolean contieneEstructuraExplicativa(String texto) {
        return texto.matches(".*(es |son |consiste|significa|se basa|implica|debe|permite).*");
    }

    private int calcularIntensidad(String texto) {
        int palabras = texto.split(" ").length;

        if (palabras > 20) return 3;
        if (palabras > 12) return 2;
        return 1;
    }

    private String limpiarTexto(String texto) {
        return texto.trim()
            .replaceAll("\\s+", " ")
            .replaceAll("[\\n\\r]", "");
    }
}
