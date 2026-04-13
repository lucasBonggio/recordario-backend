package com.recordario.preguntas.asignacion.validacion;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.recordario.compartido.enums.EjeSemantico;

public class DiccionarioSemantico {

    private static final Map<EjeSemantico, List<String>> señales = Map.of(
        EjeSemantico.EMOCION, List.of("miedo", "ansiedad", "estrés", "enojo", "felicidad", "placer", "emocion", "sentimiento", "estado emocional", "tristeza", "angustia", "alegría", "culpa", "tensión", "temor"),
        EjeSemantico.ACCION, List.of("hacer", "aplicar", "usar", "resolver", "actuar", "aplicar", "usar", "realizar", "comportamiento", "acción", "reacción", "enfrentar", "evitar", "resolver"),
        EjeSemantico.CAUSA, List.of("por qué", "causa", "motivo", "razón", "origen", "razón", "provoca", "genera", "produce", "debido a", "a partir de"),
        EjeSemantico.CONSECUENCIA, List.of("resultado", "provoca", "genera", "efecto", "consecuencia", "impacta", "afecta", "deriva en", "lleva a", "produce"),
        EjeSemantico.COMPRENSION, List.of("explicar", "entender", "significa", "comprender", "significado", "sentido", "interpretar", "describir"),
        EjeSemantico.COMPARACION, List.of("comparar", "diferencia", "similitud", "relación", "contraste", "más que", "menos que", "igual que"),
        EjeSemantico.MEMORIA, List.of("recordar", "experiencia", "alguna vez", "recuerdo", "pasado", "vivencia", "alguna vez", "anteriormente"),
        EjeSemantico.DECISION, List.of("decidir", "elegir", "opción", "elección", "optar", "juicio", "criterio")
    );


    public static Set<EjeSemantico> detectarEjes(String texto) {
        Set<EjeSemantico> ejesDetectados = new HashSet<>();
        String t = texto.toLowerCase();

        for (var entry : señales.entrySet()) {
            for (String señal : entry.getValue()) {
                if (t.contains(señal)) {
                    ejesDetectados.add(entry.getKey());
                }
            }
        }
        return ejesDetectados;
    }
}
