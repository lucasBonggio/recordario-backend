package com.recordario.repaso.dto;

import com.recordario.repaso.SesionRepaso;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSesionDTO {
    private boolean sesionFinalizada;
    private int indiceActual;
    private int totalTarjetas;

    public static EstadoSesionDTO construirEstadoSesion(SesionRepaso sesion){
        EstadoSesionDTO dto = new EstadoSesionDTO();
        dto.setSesionFinalizada(sesion.estaFinalizada());
        dto.setIndiceActual(sesion.getIndiceActual());
        dto.setTotalTarjetas(sesion.getTarjetas().size());
        
        return dto;
    }
}
