package com.recordario.cartas;

import com.recordario.compartido.enums.TipoCarta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterpretacionCarta {

    private Long idCarta;
    private TipoCarta tipo;
    private String descripcion;
    private Integer intensidad;

}
