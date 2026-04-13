package com.recordario.tarjetas.dto;

import com.recordario.tarjetas.Tarjeta;

import lombok.Data;

@Data
public class TarjetaRespuesta {
    private Long tarjetaId;
    private int calificacion;
    private int diasProximaRevision;

    public static TarjetaRespuesta mapearRespuesta(Tarjeta tarjeta, int calificacion){
        TarjetaRespuesta respuesta = new TarjetaRespuesta();
        respuesta.calificacion = calificacion;
        respuesta.diasProximaRevision = tarjeta.getIntervalo();
        respuesta.tarjetaId = tarjeta.getId();

        return respuesta;
    }

    public boolean esCorrecta(){
        return calificacion >= 3;
    }
}
