package com.recordario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.recordario.excepciones.tipos.RecursoNoEncontrado;
import com.recordario.usuarios.Usuario;
import com.recordario.usuarios.UsuarioRepositorio;

@Configuration
public class AppConfig {

    public final UsuarioRepositorio usuarioRepositorio;

    public AppConfig(UsuarioRepositorio usuarioRepositorio){
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Bean
    public PasswordEncoder codificadorContraseña(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    

    @Bean
    public UserDetailsService userDetailsService(){
        return nombreUsuario -> {
            Usuario usuario = usuarioRepositorio.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado."));
            
            return User.builder()
                        .username(usuario.getNombreUsuario())
                        .password(usuario.getContraseña())
                        .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(codificadorContraseña());
        return authProvider;
    }
}
