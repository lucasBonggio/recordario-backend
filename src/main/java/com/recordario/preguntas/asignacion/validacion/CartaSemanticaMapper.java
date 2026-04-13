package com.recordario.preguntas.asignacion.validacion;

import java.util.Set;

import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.EjeSemantico;

public class CartaSemanticaMapper {

public static Set<EjeSemantico> ejesDeCarta(Carta carta) {
    return switch (carta.getTipoCarta()) {
        case EMOCIONAL ->
            Set.of(EjeSemantico.EMOCION, EjeSemantico.MEMORIA, EjeSemantico.CAUSA);

        case ATMOSFERA ->
            Set.of(EjeSemantico.COMPRENSION, EjeSemantico.EMOCION);

        case REFLEXION ->
            Set.of(EjeSemantico.CAUSA, EjeSemantico.CONSECUENCIA, EjeSemantico.COMPRENSION);

        case TEMATICA ->
            Set.of(EjeSemantico.COMPRENSION, EjeSemantico.COMPARACION);

        case OSCURIDAD ->
            Set.of(EjeSemantico.COMPRENSION, EjeSemantico.MEMORIA, EjeSemantico.CAUSA);

        default ->
            Set.of(EjeSemantico.COMPRENSION);
    };
}

}
