package com.recordario.libro;

import java.util.ArrayList;
import java.util.List;

import com.recordario.analisis.modelos.Capitulo;
import com.recordario.analisis.modelos.IdeaRelacionada;
import com.recordario.compartido.enums.Estado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Libro {
    private Long id;
    private String titulo;
    private Estado estado;
    private List<Capitulo> capitulos = new ArrayList<>();
    private List<IdeaRelacionada> ideasRelacionadas = new ArrayList<>();
}
