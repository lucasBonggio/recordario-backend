package com.recordario.compartido;

import java.util.Arrays;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class Utilidades {
    
    public void agregarCookie(HttpServletResponse respuesta, String nombre, String valor, int maxAge){
        ResponseCookie cookie = ResponseCookie.from(nombre, valor)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(maxAge)
            .sameSite("None")
            .build();

        respuesta.addHeader("Set-Cookie", cookie.toString());
    }
    
    public String obtenerCookie(HttpServletRequest request, String nombre) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(nombre))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
