package com.recordario.preguntas.dto;

import com.recordario.compartido.enums.Tipo;
import com.recordario.preguntas.Pregunta;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreguntaDTO {
    private Long id;
    private String texto;
    private Tipo tipo;

    public static PreguntaDTO desde(Pregunta pregunta){
        return new PreguntaDTO(pregunta.getId(), pregunta.getTexto(), pregunta.getTipo());
    }
}
