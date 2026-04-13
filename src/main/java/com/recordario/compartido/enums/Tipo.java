package com.recordario.compartido.enums;

public enum Tipo {
    FACTUAL, 
    CONCEPTUAL,
    APLICACION,
    RECUERDO,
    EXPLICACION;

    public static Tipo convertirDeString(String tipo){
        if(tipo == null){
            throw new IllegalArgumentException("El tipo no puede ser nulo.");
        }

        try {
            return Tipo.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Tipo inválido: " + tipo + ". "
            );
        }
    }

}


