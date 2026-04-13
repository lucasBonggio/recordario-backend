package com.recordario.excepciones.tipos;

public class NombreUsuarioRepetido extends RuntimeException {
    public NombreUsuarioRepetido(String mensaje){
        super(mensaje);
    }

}
