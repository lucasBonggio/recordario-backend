package com.recordario.repaso;

import java.util.List;

import org.springframework.stereotype.Component;

import com.recordario.tarjetas.Tarjeta;

@Component
public class SesionRepasoMapper {
    public SesionRepasoEntidad aEntidad(String sesionId, SesionRepaso sesion){
        return new SesionRepasoEntidad(
            sesionId,
            sesion.getNombreUsuario(),
            sesion.getIndiceActual(),
            sesion.isFinalizada(),
            sesion.getTarjetas()
                        .stream()
                        .map(Tarjeta::getId)
                        .toList()
        );
    }

    public SesionRepaso aDominio(SesionRepasoEntidad entidad, List<Tarjeta> tarjetas){
        SesionRepaso sesion = new SesionRepaso(entidad.getNombreUsuario(), tarjetas);

        sesion.setIndiceActual(entidad.getIndiceActual());
        if(entidad.isFinalizada()){
            sesion.finalizar();
        }

        return sesion;
    }
}
