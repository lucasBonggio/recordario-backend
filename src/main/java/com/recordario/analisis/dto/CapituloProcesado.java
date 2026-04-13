package com.recordario.analisis.dto;

import java.util.List;

import com.recordario.cartas.Carta;

public record CapituloProcesado (
    Integer numero,
    String titulo,
    List<Carta> cartas
){} 