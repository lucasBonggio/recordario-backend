package com.recordario.tarjetas.dto;

import java.time.LocalDate;

public record RespuestaDTO (
    Long tarjetaId,
    int calificacion,
    LocalDate fechaActual
){}
