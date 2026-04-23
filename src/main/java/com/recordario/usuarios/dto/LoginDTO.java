package com.recordario.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank(message="El nombre de usuario no puede estar vacío.")
    String nombreUsuario,
    @NotBlank(message="La contraseña no puede estar vacía.")
    String contrasena
){}
