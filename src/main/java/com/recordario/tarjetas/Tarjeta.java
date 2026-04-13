package com.recordario.tarjetas;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recordario.cartas.Carta;
import com.recordario.excepciones.tipos.DatosInvalidos;
import com.recordario.repaso.dto.SmRespuesta;
import com.recordario.repaso.sm2.Sm2Servicio;
import com.recordario.tarjetas.dto.TarjetaDTO;
import com.recordario.usuarios.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Tarjeta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="usuario_id")
    @JsonBackReference("usuario-tarjetas")
    private Usuario usuario;
    private String pregunta;
    private int repeticiones;
    private int intervalo;
    private double factorFacilidad;
    private LocalDate proximaRevision;
    private LocalDate ultimaRevision;
    private String tituloCapitulo;
    private String textoCarta;
    

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="carta_id")
    private Carta carta;

    public void responder(int calificacion, LocalDate fecha, Sm2Servicio sm2Servicio){
        if(!validarCalificacion(calificacion)) throw new DatosInvalidos("Calificación inválida.");

        SmRespuesta respuesta = sm2Servicio.sm2(calificacion,
                                                intervalo,
                                                repeticiones,
                                                factorFacilidad);

        this.factorFacilidad = respuesta.getFactorFacilidadNuevo();
        this.intervalo = respuesta.getIntervalo();
        this.repeticiones = respuesta.getRepeticiones();
        this.ultimaRevision = fecha;
        this.proximaRevision = LocalDate.now().plusDays(respuesta.getIntervalo());
    }

    public Tarjeta asociarTarjeta(Usuario usuario, Carta carta){
        this.usuario = usuario;
        this.carta = carta;

        return this;
    }

    public boolean validarCalificacion(int calificacion){
        return calificacion >= 0 && calificacion <= 5;
    }

    public TarjetaDTO aDTO(){
        return new TarjetaDTO(this.getId(), 
                            this.getCarta().getId(),
                            this.getCarta().getTipoCarta(),
                            this.pregunta,
                            this.textoCarta,
                            this.tituloCapitulo,
                            this.getIntervalo());
    }

    public boolean esRepasable(){
        return this.getProximaRevision().isBefore(LocalDate.now());
    }

    public boolean repasadasHoy(){
        return this.getUltimaRevision().isEqual(LocalDate.now());
    }
    
}   
