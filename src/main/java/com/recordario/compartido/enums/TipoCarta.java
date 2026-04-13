package com.recordario.compartido.enums;

import java.util.Set;

public enum TipoCarta {

    ATMOSFERA(Set.of(Tipo.FACTUAL, Tipo.CONCEPTUAL)),
    OSCURIDAD(Set.of(Tipo.RECUERDO, Tipo.EXPLICACION)),
    REFLEXION(Set.of(Tipo.CONCEPTUAL, Tipo.EXPLICACION)),
    EMOCIONAL(Set.of(Tipo.RECUERDO, Tipo.APLICACION)), 
    TEMATICA(Set.of(Tipo.FACTUAL, Tipo.EXPLICACION)),
    EXPLICATIVA(Set.of(Tipo.CONCEPTUAL, Tipo.EXPLICACION))
    ;
    private final Set<Tipo> tiposAceptados;

    TipoCarta(Set<Tipo> tiposAceptados) {
        this.tiposAceptados = tiposAceptados;
    }

    public boolean acepta(Tipo tipoPregunta) {
        return tiposAceptados.contains(tipoPregunta);
    }
}
