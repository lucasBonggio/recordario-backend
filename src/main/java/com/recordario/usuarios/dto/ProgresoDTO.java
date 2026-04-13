package com.recordario.usuarios.dto;

public record ProgresoDTO(
    int tarjetasTotales,
    int tarjetasPendientes,
    int tarjetasRepasadasHoy
) {}
