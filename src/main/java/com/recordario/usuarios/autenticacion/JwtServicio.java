package com.recordario.usuarios.autenticacion;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix= "application.security.jwt")
@Getter
@Setter
@Component
public class JwtServicio {
    private String codigoSecreto;
    private Long tiempoExpiracion;
    private Long tiempoExpiracionRefresco;

    public String generarToken(String nombreUsuario){
        return construirToken(nombreUsuario, tiempoExpiracion);
    }

    public String generarTokenRefresco(String nombreUsuario){
        return construirToken(nombreUsuario, tiempoExpiracionRefresco);
    }

    private String construirToken(String nombreUsuario, Long expiracion){
        return Jwts.builder()
                .subject(nombreUsuario)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(obtenerCodigo())
                .compact();
    }

    private SecretKey obtenerCodigo(){
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(codigoSecreto)
        );
    }

    public boolean esValido(String token) {
        try {
            Jwts.parser()
                .verifyWith(obtenerCodigo())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public String obtenerNombreUsuario(String token) {
        try {
            return extraerClaims(token).getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Claims extraerClaims(String token){
        return Jwts.parser()
                    .verifyWith(obtenerCodigo())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
}
