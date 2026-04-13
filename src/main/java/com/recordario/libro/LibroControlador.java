package com.recordario.libro;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordario.analisis.dto.AnalisisRespuestaDTO;
import com.recordario.analisis.servicios.AnalisisLibroServicio;


@RestController
@RequestMapping("/autenticacion/libros")
public class LibroControlador {
    private final AnalisisLibroServicio analisisLibroServicio;

    public LibroControlador(AnalisisLibroServicio analisisLibroServicio){
        this.analisisLibroServicio = analisisLibroServicio;
    }

    @PostMapping("/analizar")
    public ResponseEntity<AnalisisRespuestaDTO> analizar(@RequestBody Libro libro, @AuthenticationPrincipal UserDetails userDetails){
        String nombreUsuario = userDetails.getUsername();
        
        AnalisisRespuestaDTO respuesta = analisisLibroServicio.analizarLibro(libro, nombreUsuario);

        return ResponseEntity.ok(respuesta);
    }
}