package com.recordario.preguntas.asignacion;

import java.util.Optional;

import com.recordario.analisis.modelos.CaracteristicasExtraidas;
import com.recordario.cartas.Carta;
import com.recordario.compartido.enums.TipoCarta;
import com.recordario.preguntas.Regla;

public class ReglaReflexiva implements Regla{

    @Override
    public Optional<Carta> evaluar(CaracteristicasExtraidas c) {
        if(c.getNivelReflexivo() > 6){
            return Optional.of(new Carta(TipoCarta.REFLEXION , "Muy completativo."));
        }
        
        return Optional.empty();
    }

}
