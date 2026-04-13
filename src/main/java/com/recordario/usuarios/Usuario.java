package com.recordario.usuarios;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.recordario.tarjetas.Tarjeta;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String nombreUsuario;
    private String email;
    private String contraseña;

    @OneToMany(mappedBy="usuario")
    @JsonManagedReference("usuario-tarjetas")
    private List<Tarjeta> tarjetas = new ArrayList<>();
}
