package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;

public class AnalizadorMaestro {

    private final AnalizadorEmocional analizadorEmocional;
    private final AnalizadorAtmosfera analizadorAtmosfera;
    private final AnalizadorTematico analizadorTematico;
    private final AnalizadorExplicativo analizadorExplicativo;

    public AnalizadorMaestro(){
        this.analizadorAtmosfera = new AnalizadorAtmosfera();
        this.analizadorEmocional = new AnalizadorEmocional();
        this.analizadorTematico = new AnalizadorTematico();
        this.analizadorExplicativo = new AnalizadorExplicativo();        

    }

    public List<Carta> analizar(String texto, Long idNota) {
        List<Carta> resultado = new ArrayList<>();

        resultado.addAll(analizadorAtmosfera.analizar(texto, idNota));
        resultado.addAll(analizadorEmocional.analizar(texto, idNota));
        resultado.addAll(analizadorTematico.analizar(texto, idNota));
        resultado.addAll(analizadorExplicativo.analizar(texto, idNota));

        normalizarIntensidades(resultado);
        
        if (resultado.isEmpty()) {
            resultado.add(crearCartaAuxiliar(texto, idNota));
        }

        return resultado;
    }

    private void normalizarIntensidades(List<Carta> resultado){
        for (Carta carta : resultado) {
            if(carta.getIntensidad() == null){
                carta.setIntensidad(1);
            }
        }
    }

    public Carta crearCartaAuxiliar(String texto, Long idNota){
        Carta carta = new Carta();
        carta.setIdNota(idNota);
        carta.setEmocion(null);
        carta.setTipoCarta(TipoCarta.EXPLICATIVA);
        carta.setIntensidad(1);
        carta.setDescripcion(texto);

        return carta;
    }
}
