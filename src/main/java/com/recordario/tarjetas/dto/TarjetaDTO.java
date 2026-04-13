package com.recordario.tarjetas.dto;


import com.recordario.compartido.enums.TipoCarta;

public record TarjetaDTO (
    Long tarjetaId,
    Long cartaId,
    TipoCarta tipoCarta,
    String pregunta,
    String textoCarta,
    String textoCapitulo,
    int diasHastaProximaRevision
){}
