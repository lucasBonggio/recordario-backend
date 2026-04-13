package com.recordario.libro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.recordario.analisis.dto.AnalisisLibro;
import com.recordario.analisis.dto.CapituloProcesado;
import com.recordario.analisis.modelos.Capitulo;
import com.recordario.analisis.modelos.Nota;
import com.recordario.analisis.servicios.IdeaClaveServicio;
import com.recordario.cartas.Carta;
import com.recordario.cartas.GeneradorDeCartasAvanzado;

@Service
public class LibroServicio {

    private final IdeaClaveServicio ideaClaveServicio;
    private final GeneradorDeCartasAvanzado generadorAvanzado;

    public LibroServicio(IdeaClaveServicio ideaClaveServicio, GeneradorDeCartasAvanzado generadorAvanzado){
        this.ideaClaveServicio = ideaClaveServicio;
        this.generadorAvanzado = generadorAvanzado;
    }

    public AnalisisLibro analizar(Libro libro){
        List<CapituloProcesado> respuestaCapitulos = new ArrayList<>();
        for (Capitulo cap : libro.getCapitulos()) {
            ideaClaveServicio.extraerIdeas(cap);
            List<Carta> cartas = new ArrayList<>();
            
            for (Nota nota : cap.getNotas()) {
                cartas.addAll(generadorAvanzado.generarCarta(nota.getTexto(), nota.getId()));
                
            }

            CapituloProcesado respuestaCap = armarRespuesta(cap, cartas);

            respuestaCapitulos.add(respuestaCap);
        }
        
        return new AnalisisLibro(respuestaCapitulos);
    }

    public String unirOraciones(List<Nota> notas){
        return notas.stream()
                .map(Nota::getTexto)
                .collect(Collectors.joining(". "));
    }

    public CapituloProcesado armarRespuesta(Capitulo capitulo, List<Carta> cartas){
        return new CapituloProcesado(capitulo.getNumero(), capitulo.getTitulo(), cartas);
    }
}
