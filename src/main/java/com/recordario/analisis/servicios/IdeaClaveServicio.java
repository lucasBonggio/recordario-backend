package com.recordario.analisis.servicios;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.analisis.modelos.Capitulo;
import com.recordario.analisis.modelos.IdeaClave;
import com.recordario.analisis.modelos.Nota;
import com.recordario.compartido.enums.Estado;
import com.recordario.compartido.enums.TipoCarta;

@Service
public class IdeaClaveServicio {
    private final ProcesadorTexto procesadorTexto;

    public IdeaClaveServicio(ProcesadorTexto procesadorTexto){
        this.procesadorTexto = procesadorTexto;
    }

    public void extraerIdeas(Capitulo capitulo){
        List<String> todasLasFrases = new ArrayList<>();
        
        for(Nota n: capitulo.getNotas()){
            List<String> frases = procesadorTexto.extraerFrases(n.getTexto());
            todasLasFrases.addAll(frases);
        }
        List<IdeaFrase> ideasPuntuadas = puntuarIdeas(todasLasFrases);
        List<IdeaFrase> top = obtenerTop5(ideasPuntuadas);

        for(IdeaFrase f: top){
            IdeaClave idea = new IdeaClave();
            idea.setTexto(f.texto);
            idea.setEstado(Estado.NUEVO);
            idea.setTipo(TipoCarta.OSCURIDAD);

            capitulo.getIdeasClave().add(idea);
        }
    }

    private List<IdeaFrase> puntuarIdeas(List<String> frases){
        return frases.stream()
                .map(f -> new IdeaFrase(f, procesadorTexto.puntuarFrase(f)))
                .toList();
    }

    private List<IdeaFrase> obtenerTop5(List<IdeaFrase> ideasPuntuadas){
        return ideasPuntuadas.stream()
                            .sorted(Comparator.comparingInt(IdeaFrase::puntuacion).reversed())
                            .limit(5)
                            .toList();
    }

    private record IdeaFrase(String texto, int puntuacion){}
}

