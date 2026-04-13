package com.recordario.usuarios.dto;

public record UsuarioDTO (
    Long usuarioId,
    String nombreUsuario,
    String email,
    String mensaje
){}