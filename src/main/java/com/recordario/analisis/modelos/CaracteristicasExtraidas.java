package com.recordario.analisis.modelos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaracteristicasExtraidas {
    private double promedioOscuridad;
    private double intensidadEmocional;
    private boolean esClasico;
    private boolean esDenso;
    private Set<String> temasPrincipales;
    private int cantidadIdeasClave;
    private int cantidadDeCapitulos;
    private double claridadNarrativa;
    private double nivelReflexivo;

    public CaracteristicasExtraidas(int cantidadIdeasClave, int cantidadDeCapitulos, double promedioOscuridad, double intensidadEmocional, Set<String> temasPrincipales){
        this.cantidadIdeasClave = cantidadIdeasClave;
        this.cantidadDeCapitulos = cantidadDeCapitulos;
        this.promedioOscuridad = promedioOscuridad;
        this.intensidadEmocional = intensidadEmocional;
        this.temasPrincipales = temasPrincipales;
    }
}
