package com.recordario.analisis.modelos;

import com.recordario.compartido.enums.Tipo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coincidencia {
    private int prioridad;
    private Tipo intencion;

}
