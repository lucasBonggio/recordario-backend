package com.recordario.analisis.servicios;

import org.springframework.stereotype.Service;

import com.recordario.analisis.dto.ResultadoExtraccion;
import com.recordario.analisis.modelos.Capitulo;
import com.recordario.cartas.Carta;
import com.recordario.cartas.InterpretacionCarta;
import com.recordario.libro.Libro;

@Service
public class ExtractorServicio {
    
    public ResultadoExtraccion extraer(Libro libro){
        ResultadoExtraccion analisis = new ResultadoExtraccion(libro.getId(), libro.getTitulo());

        for(Capitulo cap: libro.getCapitulos()){
            AnalisisCapitulo ac = new AnalisisCapitulo(cap.getId(), cap.getTitulo());
            
            for(Carta carta: cap.getCartas()){
                ac.agregarCartaAnalizado(
                    armarInterpretacion(carta)
                );
            }

            analisis.agregarCapituloAnalizado(ac);
        }

        return analisis;
        
    }

    public InterpretacionCarta armarInterpretacion(Carta carta){
        InterpretacionCarta interpretacion = new InterpretacionCarta();
        interpretacion.setIdCarta(carta.getId());
        interpretacion.setTipo(carta.getTipoCarta());
        interpretacion.setDescripcion(carta.getDescripcion());
        interpretacion.setIntensidad(carta.getIntensidad());

        return interpretacion;
    }
}
