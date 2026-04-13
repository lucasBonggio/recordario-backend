package com.recordario.analisis.dto;

import java.util.ArrayList;
import java.util.List;

import com.recordario.analisis.modelos.IdeaClave;
import com.recordario.analisis.servicios.AnalisisCapitulo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoExtraccion {

    private Long idLibro;
    private String tituloLibro;
    private List<IdeaClave> ideasClave;
    private List<AnalisisCapitulo> capitulosAnalizados = new ArrayList<>();

    public ResultadoExtraccion(Long idLibro, String tituloLibro){
        this.idLibro = idLibro;
        this.tituloLibro = tituloLibro;
    }
    
    public void agregarCapituloAnalizado(AnalisisCapitulo cap) {
        this.capitulosAnalizados.add(cap);
    }
}
