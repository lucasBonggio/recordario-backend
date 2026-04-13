package com.recordario.excepciones;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordario.excepciones.tipos.CredencialesIncorrectas;
import com.recordario.excepciones.tipos.DatosInvalidos;
import com.recordario.excepciones.tipos.NombreUsuarioRepetido;
import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.excepciones.tipos.SesionFinalizada;

@RestControllerAdvice
public class ManejarExcepcion {


    @ExceptionHandler(NombreUsuarioRepetido.class)
    public ResponseEntity<Map<String, Object>> manejarNombreUsuarioRepetido(NombreUsuarioRepetido excepcion){
        return construirRespuesta(HttpStatus.BAD_REQUEST, excepcion.getMessage());
    }

    @ExceptionHandler(CredencialesIncorrectas.class)
    public ResponseEntity<Map<String, Object>> manejarCredencialesIncorrectas(CredencialesIncorrectas excepcion){
        return construirRespuesta(HttpStatus.UNAUTHORIZED, excepcion.getMessage());
    }

    @ExceptionHandler(SesionFinalizada.class)
    public ResponseEntity<Map<String, Object>> manejarSesionFinalizada(SesionFinalizada excepcion){
        return construirRespuesta(HttpStatus.CONFLICT, excepcion.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontrado.class)
    public ResponseEntity<Map<String, Object>> manejarRecursosNoEncontrados(RecursoNoEncontrado excepcion){
        return construirRespuesta(HttpStatus.NOT_FOUND, excepcion.getMessage());
    }

    @ExceptionHandler(DatosInvalidos.class)
    public ResponseEntity<Map<String, Object>> manejarDatosInvalidos(DatosInvalidos excepcion){
        return construirRespuesta(HttpStatus.BAD_REQUEST, excepcion.getMessage());
    } 

    public ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus estado, String mensaje){
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        respuesta.put("marcaTiempo", LocalDateTime.now());
        respuesta.put("codigo", estado);
        respuesta.put("estado", estado.getReasonPhrase());

        return new ResponseEntity<>(respuesta, estado);
    }
}
