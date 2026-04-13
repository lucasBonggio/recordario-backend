package com.recordario.analisis.dto;

import java.util.List;

public record AnalisisLibro(
    List<CapituloProcesado> capitulos
){}
