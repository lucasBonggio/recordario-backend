package com.recordario.tarjetas.dto;

import com.recordario.cartas.Carta;

public record DatosTarjeta(
    String nombreUsuario,
    Carta carta,
    String pregunta,
    String tituloCapitulo
) {}
