package com.recordario.repaso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.recordario.excepciones.tipos.DatosInvalidos;
import com.recordario.repaso.dto.EstadoSesionDTO;
import com.recordario.repaso.dto.FeedBack;
import com.recordario.repaso.dto.MensajeRespuestaDTO;
import com.recordario.repaso.dto.RespuestaTarjetaDTO;
import com.recordario.repaso.dto.ResultadoSesion;
import com.recordario.repaso.dto.SesionInicioDTO;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaServicio;
import com.recordario.tarjetas.dto.TarjetaDTO;
import com.recordario.tarjetas.dto.TarjetaRespuesta;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioServicio;

@Service
public class SesionRepasoServicio {
    private final TarjetaServicio tarjetaServicio;
    private final OrdenarPrioridad ordenarPrioridad;
    private final UsuarioServicio usuarioServicio;
    private final GestorSesionesRepaso gestorSesiones;

    public SesionRepasoServicio(TarjetaServicio tarjetaServicio, OrdenarPrioridad ordenarPrioridad, UsuarioServicio usuarioServicio, GestorSesionesRepaso gestorSesionesRepaso){
        this.tarjetaServicio = tarjetaServicio;
        this.ordenarPrioridad = ordenarPrioridad;
        this.usuarioServicio = usuarioServicio;
        this.gestorSesiones = gestorSesionesRepaso;
    }

    public SesionInicioDTO iniciarSesion(String nombreUsuario) {
        Usuario usuario = usuarioServicio.obtenerUsuario(nombreUsuario);

        List<Tarjeta> tarjetas = ordenarTarjetaPorPrioridad(
            obtener15TarjetasRepasables(usuario),
            ordenarPrioridad
        );

        SesionRepaso sesion = new SesionRepaso(nombreUsuario, tarjetas);

        String sesionId = gestorSesiones.crearSesion(sesion);

        if(!sesion.tieneTarjetas()) {
            return SesionInicioDTO.sinTarjetas(sesionId);
        }

        return new SesionInicioDTO(
            true,
            "Sesión iniciada correctamente.",
            sesionId,
            sesion.tarjetaActual().aDTO()
        );
    }

    public TarjetaDTO tarjetaActual(String sesionId){
        SesionRepaso sesion = gestorSesiones.obtenerSesion(sesionId);

        return sesion.tarjetaActual().aDTO();
    }

    public MensajeRespuestaDTO finalizarSesion(String sesionId){
        SesionRepaso sesion = gestorSesiones.obtenerSesion(sesionId);
        if(noHaySesion(sesion)) return new MensajeRespuestaDTO("Sesión inexistente o ya finalizada.");

        sesion.finalizar();

        return new MensajeRespuestaDTO("Sesión finalizada correctamente.");
    }

    public RespuestaTarjetaDTO responderTarjeta(String sesionId, int calificacion) {
        SesionRepaso sesion = gestorSesiones.obtenerSesion(sesionId);

        System.out.println("INDICE ANTES DE RESPONDER: " + sesion.getIndiceActual());
        Tarjeta tarjetaActual = sesion.tarjetaActual();
        
        TarjetaRespuesta resultado = tarjetaServicio.responderTarjeta(
            tarjetaActual.getId(),
            calificacion,
            LocalDate.now()
        );

        if (resultado == null) {
            throw new DatosInvalidos("Respuesta inválida. Intentelo nuevamente.");
        }

        ResultadoSesion avance = sesion.avanzarSesion();

        gestorSesiones.guardarSesion(sesionId, sesion);

        return construirRespuesta(resultado, avance, sesion);
    }

    public List<Tarjeta> obtener15TarjetasRepasables(Usuario usuario){
        List<Tarjeta> tarjetas = tarjetaServicio.obtenerTarjetasPorUsuario(usuario.getNombreUsuario());

        return clasificarTarjetas(tarjetas).stream()
                                            .limit(15)
                                            .collect(Collectors.toList());
    }

    public List<Tarjeta> clasificarTarjetas(List<Tarjeta> tarjetas){
        List<Tarjeta> tarjetasRepasables = new ArrayList<>();
        for(Tarjeta tarjeta: tarjetas){
            if(!tarjeta.getProximaRevision().isAfter(LocalDate.now())){
                tarjetasRepasables.add(tarjeta);
            }
        }

        return tarjetasRepasables;
    }

    public List<Tarjeta> ordenarTarjetaPorPrioridad(List<Tarjeta> tarjetas, OrdenarPrioridad ordenarPrioridad){
        return ordenarPrioridad.ordenarTarjetas(tarjetas);  
    }

    public String construirSesionMedianteGestor(SesionRepaso sesion){
        return gestorSesiones.crearSesion(sesion);
    }

    private RespuestaTarjetaDTO construirRespuesta(TarjetaRespuesta resultado, ResultadoSesion avance, SesionRepaso sesion) {
        RespuestaTarjetaDTO dto = new RespuestaTarjetaDTO();
        dto.setFeedBack(FeedBack.construirFeedBack(resultado));

        dto.setEstadoSesion(new EstadoSesionDTO(
            avance.sesionFinalizada(),
            sesion.getIndiceActual(),
            sesion.getTarjetas().size()
        ));

        if (!avance.sesionFinalizada()) {
            dto.setTarjeta(avance.tarjetaActual().aDTO());
        }

        dto.setMensaje(
            avance.sesionFinalizada()
                ? "No se encontraron mas tarjetas. Sesión finalizada."
                : "Tarjetas correctamente devueltas."
        );

        return dto;
    }

    public boolean sinTarjetas(List<Tarjeta> tarjetas){
        return tarjetas.isEmpty();
    }

    public EstadoSesionDTO construirEstadoSesion(SesionRepaso sesion){
        EstadoSesionDTO dto = new EstadoSesionDTO();
        dto.setSesionFinalizada(sesion.estaFinalizada());
        dto.setIndiceActual(sesion.getIndiceActual());
        dto.setTotalTarjetas(sesion.getTarjetas().size());
        
        return dto;
    }

    public boolean respuestaValida(TarjetaRespuesta respuesta){
        return respuesta != null;
    }

    public boolean noHaySesion(SesionRepaso sesion){ 
        return sesion == null;
    };
}
