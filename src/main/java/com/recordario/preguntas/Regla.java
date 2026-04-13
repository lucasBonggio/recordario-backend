package com.recordario.preguntas;

import java.util.Optional;

import com.recordario.analisis.modelos.CaracteristicasExtraidas;
import com.recordario.cartas.Carta;

public interface Regla {
    Optional<Carta>  evaluar(CaracteristicasExtraidas c);
}
