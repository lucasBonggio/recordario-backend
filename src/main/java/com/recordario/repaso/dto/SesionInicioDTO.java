package com.recordario.repaso.dto;

import com.recordario.tarjetas.dto.TarjetaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SesionInicioDTO {
    private boolean hayTarjetas;
    private String mensaje;
    private String sesionId;
    private TarjetaDTO tarjeta;

    public static SesionInicioDTO sinTarjetas(String sesionId){
        return new SesionInicioDTO(false, "Sesión iniciada correctamente. No contiene tarjetas repasables.", sesionId, null);
    }
}
