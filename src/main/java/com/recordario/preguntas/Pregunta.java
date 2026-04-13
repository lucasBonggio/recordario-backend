package com.recordario.preguntas;

import com.recordario.compartido.enums.Tipo;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Pregunta {
    private Long id;
    private Long idNota;
    private String texto;
    private Tipo tipo;
    private boolean validado;
}
