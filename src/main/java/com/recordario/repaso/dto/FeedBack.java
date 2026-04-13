package com.recordario.repaso.dto;

import com.recordario.excepciones.tipos.DatosInvalidos;
import com.recordario.tarjetas.dto.TarjetaRespuesta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBack {
    private Long tarjetaRespondidaId;
    private int calificacion;
    private boolean fueCorrecta;
    
    public static FeedBack construirFeedBack(TarjetaRespuesta tr){
        if(tr == null) throw new DatosInvalidos("Respuesta inválida. Intentelo nuevamente.");
        FeedBack feedBack = new FeedBack();
        feedBack.setTarjetaRespondidaId(tr.getTarjetaId());
        feedBack.setFueCorrecta(tr.esCorrecta());
        feedBack.setCalificacion(tr.getCalificacion());

        return feedBack;
    }
}
