package com.recordario.tarjetas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordario.tarjetas.dto.TarjetaDTO;

@RestController
@RequestMapping("/autenticacion/tarjetas")
public class TarjetaControlador {
    private final TarjetaServicio tarjetaServicio;
    
    public TarjetaControlador(TarjetaServicio tarjetaServicio){
        this.tarjetaServicio = tarjetaServicio;
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<TarjetaDTO>> obtenerTarjetas(@AuthenticationPrincipal UserDetails userDetails){
        String nombreUsuario = userDetails.getUsername();

        List<Tarjeta> tarjetasRepasables = tarjetaServicio.obtenerTarjetasRepasables(nombreUsuario);

        return ResponseEntity.ok(tarjetasDTOs(tarjetasRepasables));
    }

    public List<TarjetaDTO> tarjetasDTOs(List<Tarjeta> tarjetas){
        return tarjetas.stream()
                    .map(tarjeta -> tarjeta.aDTO())
                    .toList();
    }
}
