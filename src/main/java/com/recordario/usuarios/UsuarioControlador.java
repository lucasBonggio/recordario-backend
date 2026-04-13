package com.recordario.usuarios;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordario.compartido.Utilidades;
import com.recordario.usuarios.autenticacion.JwtServicio;
import com.recordario.usuarios.dto.CrearUsuarioDTO;
import com.recordario.usuarios.dto.LoginDTO;
import com.recordario.usuarios.dto.RespuestaInicioSesion;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/publico/usuarios")
public class UsuarioControlador {
    private final UsuarioServicio usuarioServicio;
    private final JwtServicio jwtServicio;
    private final Utilidades utilidades;

    public UsuarioControlador(UsuarioServicio usuarioServicio, JwtServicio jwtServicio, Utilidades utilidades){
        this.usuarioServicio = usuarioServicio;
        this.jwtServicio = jwtServicio;
        this.utilidades = utilidades;
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaInicioSesion> logearUsuario(@RequestBody LoginDTO datosLogin, HttpServletResponse respuesta){
        RespuestaInicioSesion respuestaInicio = usuarioServicio.logearUsuario(datosLogin);

        String tokenAcceso = jwtServicio.generarToken(respuestaInicio.nombreUsuario());

        String tokenRefresco = jwtServicio.generarTokenRefresco(respuestaInicio.nombreUsuario());

        utilidades.agregarCookie(respuesta, "tokenAcceso", tokenAcceso, 15 * 60);
        utilidades.agregarCookie(respuesta, "tokenRefresco", tokenRefresco, 7 * 24 * 60 * 60);

        return ResponseEntity.ok(respuestaInicio);
    }

    @PostMapping("/registro")
    public ResponseEntity<String> crearUsuario(@RequestBody CrearUsuarioDTO nuevoUsuario){
        usuarioServicio.crearUsuario(nuevoUsuario);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente.");
    }


    @PostMapping("/refrescar")
    public ResponseEntity<?> refrescarToken(HttpServletRequest pedido, HttpServletResponse respuesta){
        String tokenRefresco = utilidades.obtenerCookie(pedido, "tokenRefresco");
        
        if (tokenRefresco == null || !jwtServicio.esValido(tokenRefresco)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String nombreUsuario = jwtServicio.obtenerNombreUsuario(tokenRefresco);

        String nuevoTokenAcceso = jwtServicio.generarToken(nombreUsuario);

        utilidades.agregarCookie(respuesta, "tokenAcceso", nuevoTokenAcceso, 15 * 60);

        return ResponseEntity.ok().build();
    }

}
