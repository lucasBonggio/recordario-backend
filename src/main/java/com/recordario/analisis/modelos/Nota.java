package com.recordario.analisis.modelos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Nota {
    private Long id;
    private String texto;
    private String textoProcesado;
    private LocalDateTime creado;
}
