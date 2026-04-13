package com.recordario.cartas;

import com.recordario.compartido.enums.TipoCarta;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long idNota;
    private TipoCarta tipoCarta;
    private String emocion;
    private String descripcion;
    private Integer intensidad;

    public Carta(TipoCarta tipo, String descripcion){
        this.tipoCarta = tipo;
        this.descripcion = descripcion;
    }

    public Carta(TipoCarta tipo, String descripcion, Integer intensidad) {
        this.tipoCarta = tipo;
        this.descripcion = descripcion;
        this.intensidad = intensidad;
    }
}
