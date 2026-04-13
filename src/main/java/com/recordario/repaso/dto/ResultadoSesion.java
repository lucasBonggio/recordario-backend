package com.recordario.repaso.dto;

import com.recordario.tarjetas.Tarjeta;

public record ResultadoSesion(
    Tarjeta tarjetaActual,
    boolean sesionFinalizada
) {}