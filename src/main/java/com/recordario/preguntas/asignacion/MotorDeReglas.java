package com.recordario.preguntas.asignacion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.recordario.analisis.modelos.CaracteristicasExtraidas;
import com.recordario.cartas.Carta;
import com.recordario.preguntas.Regla;

@Component
public class MotorDeReglas {
    private final List<Regla> reglas;

    @Autowired
    public MotorDeReglas(List<Regla> reglas){
        this.reglas = reglas;
    }

    public List<Carta> procesar(CaracteristicasExtraidas c){
        List<Carta> cartas = new ArrayList<>();

        for(Regla r: reglas){
            r.evaluar(c).ifPresent(cartas::add);
        }
        
        return cartas;
    }
}
