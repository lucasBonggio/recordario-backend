package com.recordario.usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarContrasena {
    @NotBlank(message="La contraseña actual no debe estar vacía.")
    private String contrasenaActual;
    @NotBlank(message="La contraseña nueva no debe estar vacía.")
    private String nuevaContrasena;
}
