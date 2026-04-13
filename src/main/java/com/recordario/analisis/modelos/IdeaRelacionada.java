package com.recordario.analisis.modelos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdeaRelacionada {
    private Long id;
    private IdeaClave ideaA;
    private IdeaClave ideaB;
    private float puntuacion;
}
