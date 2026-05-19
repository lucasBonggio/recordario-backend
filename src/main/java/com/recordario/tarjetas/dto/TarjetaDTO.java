package com.recordario.tarjetas.dto;

public record TarjetaDTO (
    Long tarjetaId,
    String pregunta,
    String tituloTema,
    String puntosPrincipales,
    int diasHastaProximaRevision
){}
