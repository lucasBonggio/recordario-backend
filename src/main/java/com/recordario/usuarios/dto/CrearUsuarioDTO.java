package com.recordario.usuarios.dto;

public record CrearUsuarioDTO(
    String nombreUsuario,
    String contrasena,
    String email
){}
