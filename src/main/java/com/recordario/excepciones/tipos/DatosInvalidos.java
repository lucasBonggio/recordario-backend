package com.recordario.excepciones.tipos;

public class DatosInvalidos extends RuntimeException {
    public DatosInvalidos(String mensaje){
        super(mensaje);
    }
}
