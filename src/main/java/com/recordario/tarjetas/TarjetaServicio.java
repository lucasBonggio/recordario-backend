package com.recordario.tarjetas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.repaso.sm2.Sm2Servicio;
import com.recordario.tarjetas.dto.DatosTarjeta;
import com.recordario.tarjetas.dto.TarjetaRespuesta;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioServicio;

@Service
public class TarjetaServicio {

    private final TarjetaRepositorio tarjetaRepositorio;
    private final Sm2Servicio sm2Servicio;
    private final UsuarioServicio usuarioServicio;

    public TarjetaServicio(TarjetaRepositorio tarjetaRepositorio, Sm2Servicio sm2Servicio, UsuarioServicio usuarioServicio){
        this.tarjetaRepositorio = tarjetaRepositorio;
        this.sm2Servicio = sm2Servicio;
        this.usuarioServicio = usuarioServicio;
    }

    public Tarjeta crearTarjeta(DatosTarjeta datos){
        return tarjetaRepositorio.save(armarTarjeta(datos));
    }

    public Tarjeta obtenerTarjeta(Long tarjetaId){
        Tarjeta tarjetaEncontrada = tarjetaRepositorio.findById(tarjetaId)
            .orElseThrow(() -> new RecursoNoEncontrado("Tarjeta no encontrada."));
    
        return tarjetaEncontrada;
    }

    public List<Tarjeta> obtenerTarjetasPorIds(List<Long> tarjetasIds){
        return tarjetaRepositorio.findAllById(tarjetasIds);
    }
    
    public List<Tarjeta> obtenerTarjetasRepasables(String username){
        return diferenciarTarjetasRepasables(obtenerTarjetasPorUsuario(username));
    }

    public List<Tarjeta> obtenerTarjetasPorUsuario(String username){
        Usuario usuario = usuarioServicio.obtenerUsuario(username);

        return tarjetaRepositorio.findAllByUsuario(usuario);
    }
    
    public TarjetaRespuesta responderTarjeta(Long id, int calificacion, LocalDate fecha){
        Tarjeta tarjeta = tarjetaRepositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontrado("Tarjeta no encontrada."));
            
        tarjeta.responder(calificacion, fecha, sm2Servicio);
        tarjetaRepositorio.save(tarjeta);

        return TarjetaRespuesta.mapearRespuesta(tarjeta, calificacion);
    }
    
    public Tarjeta armarTarjeta(DatosTarjeta datos){
        Usuario usuario = usuarioServicio.obtenerUsuario(datos.nombreUsuario());

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setUsuario(usuario);
        tarjeta.setCarta(datos.carta());
        tarjeta.setPregunta(datos.pregunta());
        tarjeta.setIntervalo(0);
        tarjeta.setRepeticiones(0);
        tarjeta.setFactorFacilidad(2.5);
        tarjeta.setUltimaRevision(null);
        tarjeta.setProximaRevision(LocalDate.now());
        tarjeta.setTextoCarta(datos.carta().getDescripcion());
        tarjeta.setTituloCapitulo(datos.tituloCapitulo());

        return tarjeta;
    }

    public List<Tarjeta> diferenciarTarjetasRepasables(List<Tarjeta> tarjetas){
        List<Tarjeta> tarjetasRepasables = new ArrayList<>();
        for (Tarjeta tarj : tarjetas) {
            if(esRapasable(tarj)){
                tarjetasRepasables.add(tarj);
            }
        }
        return tarjetasRepasables.stream()
                                .limit(10)
                                .toList();
    }

    public boolean esRapasable(Tarjeta tarjeta){
        return tarjeta.getProximaRevision().isBefore(LocalDate.now()) || tarjeta.getProximaRevision().equals(LocalDate.now());
    }
}
