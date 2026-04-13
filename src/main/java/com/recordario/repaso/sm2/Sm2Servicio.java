package com.recordario.repaso.sm2;

import org.springframework.stereotype.Service;

import com.recordario.repaso.dto.SmRespuesta;

@Service
public class Sm2Servicio { 

    public SmRespuesta sm2(int calificacion, int intervaloActual, int repeticionesActuales, double factorFacilidadActual) {
        SmRespuesta r = new SmRespuesta();
        r.setFactorFacilidadPrevio(factorFacilidadActual);
        r.setIntervalo(intervaloActual);
        r.setCalificacion(calificacion);

        if (calificacion < 3) {
            r.setRepeticiones(0);
            r.setIntervalo(1);
            return r;
        }

        int repeticiones = repeticionesActuales + 1; 

        double ef = factorFacilidadActual +
            (0.1 - (5 - calificacion) * (0.08 + (5 - calificacion) * 0.02));
        
        if (ef < 1.3) ef = 1.3;

        int intervalo;
        intervalo = (int) (switch (repeticiones) {
            case 1 -> 1;
            case 2 -> 6;
            default -> Math.round(intervaloActual * ef);
        });

        r.setRepeticiones(repeticiones);
        r.setIntervalo(intervalo);
        r.setFactorFacilidadNuevo(ef); 
        return r;
    }
}