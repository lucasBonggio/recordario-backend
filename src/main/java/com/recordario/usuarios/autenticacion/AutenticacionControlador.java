package com.recordario.usuarios.autenticacion;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recordario.compartido.Utilidades;
import com.recordario.usuarios.UsuarioServicio;
import com.recordario.usuarios.dto.CambiarContrasena;
import com.recordario.usuarios.dto.ProgresoDTO;
import com.recordario.usuarios.dto.UsuarioDTO;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/autenticacion/usuario")
public class AutenticacionControlador {
    private final UsuarioServicio usuarioServicio;
    private final Utilidades utilidades;

    public AutenticacionControlador(UsuarioServicio usuarioServicio, Utilidades utilidades){
        this.usuarioServicio = usuarioServicio;
        this.utilidades = utilidades;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> cerrarSesion(HttpServletResponse respuesta){
        utilidades.agregarCookie(respuesta, "tokenAcceso", null, 0);
        utilidades.agregarCookie(respuesta, "tokenRefresco", null, 0);

        return ResponseEntity.ok("Sesión finalizada correctamente.");
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> obtenerDatos(@AuthenticationPrincipal UserDetails userDetails){
        String nombreUsuario = userDetails.getUsername();

        UsuarioDTO usuario = usuarioServicio.obtenerDatos(nombreUsuario);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/contraseña")
    public ResponseEntity<String> actualizarContrasena(@RequestBody CambiarContrasena datos, @AuthenticationPrincipal UserDetails userDetails){
        String nombreUsuario = userDetails.getUsername();

        usuarioServicio.actualizarContrasena(nombreUsuario, datos);

        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }
    
    @GetMapping("/progreso")
    public ResponseEntity<ProgresoDTO> obtenerProgreso(@AuthenticationPrincipal UserDetails userDetails){
        String nombreUsuario = userDetails.getUsername();

        ProgresoDTO progreso = usuarioServicio.obtenerProgreso(nombreUsuario);

        return ResponseEntity.ok(progreso);
    }
}


