package com.recordario.cartas;

import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.analisis.servicios.AnalizadorMaestro;

@Service
public class GeneradorDeCartasAvanzado implements GeneradorCarta {

    private final AnalizadorMaestro analizadorMaestro;

    public GeneradorDeCartasAvanzado(){
        this.analizadorMaestro = new AnalizadorMaestro();
    }

    @Override
    public List<Carta> generarCarta(String texto, Long idNota) {
        return analizadorMaestro.analizar(texto, idNota);
    }
}
