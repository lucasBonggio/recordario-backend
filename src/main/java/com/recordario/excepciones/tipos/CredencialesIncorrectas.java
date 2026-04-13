package com.recordario.excepciones.tipos;

public class CredencialesIncorrectas extends RuntimeException{
    public CredencialesIncorrectas(String mensaje){
        super(mensaje);
    }
}
