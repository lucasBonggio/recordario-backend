package com.recordario;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.recordario.cartas.Carta;
import com.recordario.excepciones.tipos.CredencialesIncorrectas;
import com.recordario.excepciones.tipos.NombreUsuarioRepetido;
import com.recordario.tarjetas.Tarjeta;
import com.recordario.tarjetas.TarjetaRepositorio;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioRepositorio;
import com.recordario.usuarios.UsuarioServicio;
import com.recordario.usuarios.dto.CrearUsuarioDTO;
import com.recordario.usuarios.dto.LoginDTO;
import com.recordario.usuarios.dto.ProgresoDTO;
import com.recordario.usuarios.dto.RespuestaInicioSesion;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UsuarioTestIntegracion {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TarjetaRepositorio tarjetaRepositorio;

    @Autowired
    private PasswordEncoder codificadorContraseña;


    @Deprecated
    @Test
    void crearUsuario(){
        CrearUsuarioDTO datosUsuario = new CrearUsuarioDTO(
            "usuarioTest",
            "contraseñaTest",
            "email@gmail.com");

        usuarioServicio.crearUsuario(datosUsuario);
        
    }

    @Test
    void crearUsuario_nombreEnUso(){

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("usuarioRepetido");
        usuario.setContraseña("contraseña");
        
        usuarioRepositorio.save(usuario);

        CrearUsuarioDTO datos = new CrearUsuarioDTO(
            "usuarioRepetido",
            "contraseñaTest",
            "email@gmail.com"
        );

        NombreUsuarioRepetido excepcion = assertThrows(NombreUsuarioRepetido.class, 
            () -> {
                usuarioServicio.crearUsuario(datos);
            }
        );

        assertEquals("El nombre de usuario ya está en uso.", excepcion.getMessage());
    }

    @Test
    void logearUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuarioTest");
        usuario.setContraseña(codificadorContraseña.encode("contraseñaTest"));

        usuarioRepositorio.save(usuario);

        LoginDTO datos = new LoginDTO("nombreUsuarioTest", "contraseñaTest");

        RespuestaInicioSesion respuesta = usuarioServicio.logearUsuario(datos);

        assertNotNull(respuesta);
        assertEquals("Usuario logeado correctamente.", respuesta.mensaje());
    }

    @Test
    void logearUsuario_credencialesIncorrectas(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuarioTest");
        usuario.setContraseña(codificadorContraseña.encode("contraseñaTest"));

        usuarioRepositorio.save(usuario);

        LoginDTO datos  = new LoginDTO("nombreUsuarioTest", "contraseñaIncorrecta");

        CredencialesIncorrectas excepcion = assertThrows(CredencialesIncorrectas.class, 
            () -> {
                usuarioServicio.logearUsuario(datos);
            }
        );

        assertEquals("Credenciales incorrectas.", excepcion.getMessage());
    }

    @Test
    void obtenerProgreso(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuarioRepositorio.save(usuario);

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCarta(new Carta());
        tarjeta.setProximaRevision(LocalDate.now().minusDays(3));
        tarjeta.setUsuario(usuario);
        tarjetaRepositorio.save(tarjeta);
    
        Tarjeta tarjeta2 = new Tarjeta();
        tarjeta2.setCarta(new Carta());
        tarjeta2.setProximaRevision(LocalDate.now().minusDays(3));
        tarjeta2.setUsuario(usuario);
        tarjetaRepositorio.save(tarjeta2);

        Tarjeta tarjeta3 = new Tarjeta();
        tarjeta3.setCarta(new Carta());
        tarjeta3.setProximaRevision(LocalDate.now().minusDays(7));
        //Respondida hoy
        tarjeta3.setUltimaRevision(LocalDate.now());
        tarjeta3.setUsuario(usuario);
        tarjetaRepositorio.save(tarjeta3);

        ProgresoDTO progreso = usuarioServicio.obtenerProgreso("usuarioTest");

        assertNotNull(progreso);

        assertEquals(3, progreso.tarjetasTotales());
        assertEquals(2, progreso.tarjetasPendientes());
        assertEquals(1, progreso.tarjetasRepasadasHoy());
    }
}
