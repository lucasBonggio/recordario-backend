package com.recordario.repaso;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordario.compartido.Utilidades;
import com.recordario.repaso.dto.MensajeRespuestaDTO;
import com.recordario.repaso.dto.RespuestaTarjetaDTO;
import com.recordario.repaso.dto.SesionInicioDTO;
import com.recordario.tarjetas.dto.TarjetaDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/autenticacion/repaso")
public class SesionRepasoControlador {
    private final SesionRepasoServicio sesionServicio;
    private final Utilidades utilidades;
    
    public SesionRepasoControlador(SesionRepasoServicio sesionRepasoServicio, Utilidades utilidades){
        this.sesionServicio = sesionRepasoServicio;
        this.utilidades = utilidades;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<SesionInicioDTO> iniciarSesionRepaso(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse respuesta){
        String nombreUsuario = userDetails.getUsername();

        SesionInicioDTO sesion = sesionServicio.iniciarSesion(nombreUsuario);
        
        utilidades.agregarCookie(respuesta, "sesionId", sesion.getSesionId(), 15 * 60);
        
        return ResponseEntity.ok(sesion);
    }

    @PostMapping("/sesion/responder")
    public ResponseEntity<RespuestaTarjetaDTO> responderTarjeta(@RequestBody int calificacion, HttpServletRequest pedido){
        String sesionId = utilidades.obtenerCookie(pedido, "sesionId");
        
        RespuestaTarjetaDTO respuesta = sesionServicio.responderTarjeta(sesionId, calificacion);

        return ResponseEntity.ok(respuesta); 
    }

    @GetMapping("/sesion/tarjeta")
    public ResponseEntity<TarjetaDTO> tarjetaActual(HttpServletRequest pedido){
        String sesionId = utilidades.obtenerCookie(pedido, "sesionId");

        TarjetaDTO tarjeta = sesionServicio.tarjetaActual(sesionId);

        if(tarjeta == null){
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(tarjeta);
    }

    @PostMapping("/sesion/finalizar")
    public ResponseEntity<MensajeRespuestaDTO> finalizarSesion(HttpServletRequest pedido){
        String sesionId = utilidades.obtenerCookie(pedido, "sesionId");

        MensajeRespuestaDTO respuesta = sesionServicio.finalizarSesion(sesionId);
        
        return ResponseEntity.ok(respuesta);
    }
}
