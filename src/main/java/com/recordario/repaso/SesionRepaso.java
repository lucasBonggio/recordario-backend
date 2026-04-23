package com.recordario.repaso;

import java.util.List;

import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.excepciones.tipos.SesionFinalizada;
import com.recordario.repaso.dto.ResultadoSesion;
import com.recordario.tarjetas.Tarjeta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SesionRepaso {

    private String nombreUsuario;
    private List<Tarjeta> tarjetas;
    private int indiceActual;
    private boolean finalizada;

    public SesionRepaso(String nombreUsuario, List<Tarjeta> tarjetas) {
        this.nombreUsuario = nombreUsuario;
        this.tarjetas = tarjetas;
        this.indiceActual = 0;
        this.finalizada = tarjetas.isEmpty();
    }

    public Tarjeta tarjetaActual() {
        validarSesionActiva();
        return tarjetas.get(indiceActual);
    }

    public ResultadoSesion avanzarSesion() {
        validarSesionActiva();

        indiceActual++;

        if (indiceActual >= tarjetas.size()) {
            finalizar();
            return new ResultadoSesion(null, true);
        }

        return new ResultadoSesion(tarjetas.get(indiceActual), false);
    }

    private void validarSesionActiva() {
        if (finalizada) {
            throw new SesionFinalizada("La sesión está finalizada.");
        }
        if (tarjetas == null || tarjetas.isEmpty()) {
            throw new RecursoNoEncontrado("La sesión no tiene tarjetas.");
        }
    }

    public void finalizar() {
        this.finalizada = true;
    }

    public boolean estaFinalizada() {
        return finalizada;
    }

    public boolean tieneTarjetas() {
        return !this.tarjetas.isEmpty();
    }
}
