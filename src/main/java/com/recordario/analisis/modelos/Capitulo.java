package com.recordario.analisis.modelos;

import java.util.ArrayList;
import java.util.List;

import com.recordario.cartas.Carta;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Capitulo {
    private Long id;
    private String titulo;
    private int numero;
    @Builder.Default
    private List<Nota> notas = new ArrayList<>();
    @Builder.Default
    private List<IdeaClave> ideasClave = new ArrayList<>();

    @Builder.Default
    private List<Carta> cartas = new ArrayList<>();
}
