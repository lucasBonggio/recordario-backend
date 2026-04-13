package com.recordario.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarContrasena {
    private String contrasenaActual;
    private String nuevaContrasena;
}
