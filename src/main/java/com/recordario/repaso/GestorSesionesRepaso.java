package com.recordario.repaso;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaServicio;

@Service
public class GestorSesionesRepaso {
    private final SesionRepasoRepositorio sesionRepositorio;
    private final TarjetaServicio tarjetaServicio;
    private final SesionRepasoMapper sesionMapper;

    public GestorSesionesRepaso(SesionRepasoRepositorio sesionRepasoRepositorio,TarjetaServicio tarjetaServicio, SesionRepasoMapper sesionRepasoMapper){
        this.sesionRepositorio = sesionRepasoRepositorio;
        this.tarjetaServicio = tarjetaServicio;
        this.sesionMapper = sesionRepasoMapper;
    };

    public String crearSesion(SesionRepaso sesion){
        String sesionId = UUID.randomUUID().toString();

        SesionRepasoEntidad sesionEntidad = sesionMapper.aEntidad(sesionId, sesion);

        sesionRepositorio.save(sesionEntidad);

        return sesionId;
    }

    public SesionRepaso obtenerSesion(String sesionId){
        SesionRepasoEntidad entidad = sesionRepositorio.findById(sesionId)
            .orElseThrow(() -> new RecursoNoEncontrado("Sesión no encontrada."));

        List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasPorIds(entidad.getTarjetaIds());
        
        SesionRepaso dominio = sesionMapper.aDominio(entidad, tarjetas);

        return dominio;
    }

    public void eliminarSesion(String sesionId){
        SesionRepasoEntidad entidad = sesionRepositorio.findById(sesionId)
            .orElseThrow(() -> new RecursoNoEncontrado("Sesión no encontrada."));

        sesionRepositorio.delete(entidad);
    }
    
    public void guardarSesion(String sesionId, SesionRepaso sesion){
        SesionRepasoEntidad entidad = sesionMapper.aEntidad(sesionId, sesion);
        sesionRepositorio.save(entidad);
    }

}
