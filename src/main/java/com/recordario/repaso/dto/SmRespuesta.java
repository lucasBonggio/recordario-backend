package com.recordario.repaso.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmRespuesta {
    private int calificacion;
    private int repeticiones;
    private int intervaloPrevio;
    private double factorFacilidadPrevio;
    private double factorFacilidadNuevo;
    private int intervalo;
}
