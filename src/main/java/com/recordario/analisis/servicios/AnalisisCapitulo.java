package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.List;

import com.recordario.cartas.InterpretacionCarta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalisisCapitulo {
    private Long idCapitulo;
    private String tituloCapitulo;
    private List<InterpretacionCarta> cartasAnalizadas = new ArrayList<>();
    
    public AnalisisCapitulo(Long idCapitulo, String tituloCapitulo) {
        this.idCapitulo = idCapitulo;
        this.tituloCapitulo = tituloCapitulo;
    }

    public void agregarCartaAnalizado(InterpretacionCarta  carta) {
        this.cartasAnalizadas.add(carta);
    }
}
