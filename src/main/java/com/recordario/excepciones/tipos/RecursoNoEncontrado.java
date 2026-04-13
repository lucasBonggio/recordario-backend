package com.recordario.excepciones.tipos;

public class RecursoNoEncontrado extends RuntimeException{
    public RecursoNoEncontrado(String mensaje){
        super(mensaje);
    };
}
