package com.recordario.usuarios;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordario.excepciones.tipos.CredencialesIncorrectas;
import com.recordario.excepciones.tipos.NombreUsuarioRepetido;
import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.usuarios.dto.CambiarContrasena;
import com.recordario.usuarios.dto.CrearUsuarioDTO;
import com.recordario.usuarios.dto.LoginDTO;
import com.recordario.usuarios.dto.ProgresoDTO;
import com.recordario.usuarios.dto.RespuestaInicioSesion;
import com.recordario.usuarios.dto.UsuarioDTO;

@Service
public class UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder codificadorContraseña;
    private final AuthenticationManager authenticationManager;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, PasswordEncoder codificador, AuthenticationManager authentication){
        this.usuarioRepositorio = usuarioRepositorio;
        this.codificadorContraseña = codificador;
        this.authenticationManager = authentication;
    }

    public void crearUsuario(CrearUsuarioDTO datosUsuario){
        usuarioRepositorio.save(construirUsuario(datosUsuario));
    }

    public RespuestaInicioSesion logearUsuario(LoginDTO datosLogin){
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(datosLogin.nombreUsuario(), 
                                                        datosLogin.contrasena())
            );
        } catch (AuthenticationException e) {
            throw new CredencialesIncorrectas("Credenciales incorrectas.");
        }

        Usuario usuario = buscarUsuario(datosLogin.nombreUsuario());

        return new RespuestaInicioSesion(usuario.getNombreUsuario(), "Usuario logeado correctamente.");
    }

    @Transactional
    public void actualizarContrasena(String nombreUsuario, CambiarContrasena datos){
        Usuario usuario = buscarUsuario(nombreUsuario);

        if(!codificadorContraseña.matches(datos.getContrasenaActual(), usuario.getContraseña())){
            throw new CredencialesIncorrectas("La contraseña es incorrecta.");
        }
        
        usuario.setContraseña(codificadorContraseña.encode(datos.getNuevaContrasena()));
        usuarioRepositorio.save(usuario);
    }

    private Usuario buscarUsuario(String nombreUsuario){
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado."));
    }

    public UsuarioDTO obtenerDatos(String nombreUsuario){
        Usuario usuario = obtenerUsuario(nombreUsuario);

        return mapearDTO(usuario);
    }
    
    public ProgresoDTO obtenerProgreso(String nombreUsuario){
        Usuario usuario = obtenerUsuario(nombreUsuario);

        return construirRespuesta(usuario);
    }
    
    private UsuarioDTO mapearDTO(Usuario usuario){
        return new UsuarioDTO(usuario.getId(), usuario.getNombreUsuario(), usuario.getEmail(), "Datos obtenidos correctamente.");
    }

    private ProgresoDTO construirRespuesta(Usuario usuario) {
        Long usuarioId = usuario.getId();
        
        int[] contadores = new int[3]; 
        contadores[0] = usuarioRepositorio.countTarjetasTotales(usuarioId);
        contadores[1] = usuarioRepositorio.countTarjetasPendientes(usuarioId);
        contadores[2] = usuarioRepositorio.countTarjetasRepasadasHoy(usuarioId);
        
        return new ProgresoDTO(contadores[0], contadores[1], contadores[2]);
    }

    public Usuario obtenerUsuario(String nombreUsuario){
        Usuario usuario = usuarioRepositorio.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado."));
        
        return usuario;
    }
    
    private Usuario construirUsuario(CrearUsuarioDTO datos){
        if(existeUsuario(datos.nombreUsuario())) throw new NombreUsuarioRepetido("El nombre de usuario ya está en uso.");

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(datos.nombreUsuario());
        usuario.setContraseña(codificadorContraseña.encode(datos.contrasena()));
        usuario.setEmail(datos.email());

        return usuario;
    } 

    public boolean existeUsuario(String nombreUsuario){
        return usuarioRepositorio.existsByNombreUsuario(nombreUsuario);
    }
}
