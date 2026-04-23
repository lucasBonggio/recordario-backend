package com.recordario.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearUsuarioDTO(
    @NotBlank(message="El nombre de usuario de puede estar vacío.")
    String nombreUsuario,
    @NotBlank(message="La contraseña de puede estar vacía.")
    String contrasena,
    @Email
    String email
){}
