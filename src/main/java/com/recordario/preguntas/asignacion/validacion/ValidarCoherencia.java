package com.recordario.preguntas.asignacion.validacion;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.EjeSemantico;
import com.recordario.preguntas.Pregunta;

public class ValidarCoherencia implements ValidarPregunta {

    @Override
    public boolean valida(Pregunta pregunta, Carta carta, CapituloProcesado analisisCap, List<Pregunta> preguntasDelCapitulo) {
        if (pregunta == null || pregunta.getTexto() == null) return false;

        Map<EjeSemantico, Set<EjeSemantico>> compatibilidad = Map.of(
            EjeSemantico.CAUSA, Set.of(EjeSemantico.CONSECUENCIA),
            EjeSemantico.CONSECUENCIA, Set.of(EjeSemantico.CAUSA),
            EjeSemantico.EMOCION, Set.of(EjeSemantico.CAUSA, EjeSemantico.CONSECUENCIA),
            EjeSemantico.DECISION, Set.of(EjeSemantico.CONSECUENCIA)
        );

        Set<EjeSemantico> ejesPregunta =
            DiccionarioSemantico.detectarEjes(pregunta.getTexto());

        if(ejesPregunta.isEmpty()) return true;

        Set<EjeSemantico> ejesCarta =
            CartaSemanticaMapper.ejesDeCarta(carta);

        for (EjeSemantico ejePregunta : ejesPregunta) {
            if (ejesCarta.contains(ejePregunta)) return true;

            Set<EjeSemantico> compatibles = compatibilidad.getOrDefault(ejePregunta, Set.of());
            for (EjeSemantico ejeCarta : ejesCarta) {
                if (compatibles.contains(ejeCarta)) return true;
            }
        }
        return false;
    }
}
