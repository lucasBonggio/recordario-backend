package com.recordario.compartido;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class Utilidades {
    
    public void agregarCookie(HttpServletResponse respuesta, String nombre, String valor, int maxAge){
        Cookie cookie = new Cookie(nombre, valor);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        
        respuesta.addCookie(cookie);
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
