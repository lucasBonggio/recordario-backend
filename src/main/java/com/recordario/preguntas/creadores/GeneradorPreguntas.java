package com.recordario.preguntas.creadores;

import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.cartas.Carta;
import com.recordario.preguntas.Pregunta;

@Service
public class GeneradorPreguntas {

    private final List<FabricadorPreguntas> fabricadores = List.of(
        new FabricaPreguntaAtmosfera(),
        new FabricaPreguntaEmocional(),
        new FabricaPreguntaTematica(),
        new FabricaPreguntaExplicativa()
    );

    public Pregunta generarPregunta(Carta carta){
        for (FabricadorPreguntas fabricador : fabricadores) {
            if (fabricador.aplicaA(carta.getTipoCarta())) {
                return fabricador.generar(carta);
            }
        }
        return null;
    }
}
