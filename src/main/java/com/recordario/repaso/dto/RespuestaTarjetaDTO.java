package com.recordario.repaso.dto;

import com.recordario.repaso.SesionRepaso;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.dto.TarjetaDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaTarjetaDTO {
    private FeedBack feedBack;
    private EstadoSesionDTO estadoSesion;
    private TarjetaDTO tarjeta;
    private String mensaje;

    public void hayTarjetas(SesionRepaso sesion, Tarjeta tarjeta){
        if(tarjeta == null){
            sesion.setFinalizada(true);
            this.setTarjeta(null);
            this.setEstadoSesion(EstadoSesionDTO.construirEstadoSesion(sesion));
            this.setMensaje("No se encontraron mas tarjetas. Sesión finalizada.");
        }else{
            this.setEstadoSesion(EstadoSesionDTO.construirEstadoSesion(sesion));
            this.setTarjeta(tarjeta.aDTO());
            this.setMensaje("Tarjetas correctamente devueltas.");
        }  
    }
}
