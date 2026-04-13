package com.recordario.excepciones.tipos;

public class SesionFinalizada extends RuntimeException {
    public SesionFinalizada(String mensaje){
        super(mensaje);
    }
}
