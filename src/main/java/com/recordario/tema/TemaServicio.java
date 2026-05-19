package com.recordario.tema;

import org.springframework.stereotype.Service;

import com.recordario.cartas.Carta;
import com.recordario.tarjetas.TarjetaServicio;
import com.recordario.tarjetas.dto.DatosTarjeta;

@Service
public class TemaServicio {

    private final TarjetaServicio tarjetaServicio;
    private final PuntosImportantesServicio puntosServicio;

    public TemaServicio(TarjetaServicio tarjetaServicio, PuntosImportantesServicio puntosImportantes){
        this.tarjetaServicio = tarjetaServicio;
        this.puntosServicio = puntosImportantes;
    }

    public String crearTarjeta(Tema tema, String nombreUsuario){
        Carta carta = crearCarta(tema);    

        DatosTarjeta datosTarjeta = new DatosTarjeta(
            nombreUsuario,
            carta
        );

        tarjetaServicio.crearTarjeta(datosTarjeta);

        return "Tarjeta creada satisfactoriamente.";
    }

    private Carta crearCarta(Tema tema){
        if(tema.getIdeasPrincipales().isEmpty()) return crearCartaAuxiliar(tema);
        String descripcionTema = puntosServicio.unirPuntosImportantes(tema.getIdeasPrincipales()); 

        return crearCartaCompleta(tema.getTitulo(), descripcionTema);
    }

    private Carta crearCartaAuxiliar(Tema tema){
        return new Carta(tema.getTitulo());
    }

    private Carta crearCartaCompleta(String titulo, String descripcion){
        return new Carta(titulo, descripcion);
    }
}
