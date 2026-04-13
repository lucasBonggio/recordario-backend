package com.recordario.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.recordario.usuarios.autenticacion.JwtServicio;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFiltro extends OncePerRequestFilter {
    
    private final JwtServicio jwtServicio;
    private final UserDetailsService userDetailsServicio;
    private static final String PROTECTED_PATH = "/api/v1/autenticacion";

    public JwtFiltro(JwtServicio jwtServicio, UserDetailsService userDetailsServicio){
        this.jwtServicio = jwtServicio;
        this.userDetailsServicio = userDetailsServicio;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        return !path.startsWith(PROTECTED_PATH);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = obtenerTokenDesdeCookie(request);

        if (jwt != null && jwtServicio.esValido(jwt)) {

            String nombreUsuario =
                    jwtServicio.obtenerNombreUsuario(jwt);

            if (nombreUsuario != null &&
                SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsServicio
                            .loadUserByUsername(nombreUsuario);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    public String obtenerTokenDesdeCookie(HttpServletRequest request){
        if(request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("tokenAcceso"))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
    }
}
