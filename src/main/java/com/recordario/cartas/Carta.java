package com.recordario.cartas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Carta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String tituloTema;
    private String puntosPrincipales;

    public Carta(String tituloTema) {
        this.tituloTema = tituloTema;
    }
    public Carta(String tituloTema, String puntosPrincipales) {
        this.tituloTema = tituloTema;
        this.puntosPrincipales = puntosPrincipales;
    }
}
