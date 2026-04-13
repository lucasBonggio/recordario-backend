package com.recordario.repaso;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="sesiones_repaso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SesionRepasoEntidad {
    @Id
    private String id;

    @Column(nullable=false)
    private String nombreUsuario;

    @Column(nullable=false)
    private int indiceActual;

    @Column(nullable=false)
    private boolean finalizada;

    @ElementCollection
    @CollectionTable(
        name="sesion_tarjetas",
        joinColumns= @JoinColumn(name="sesion_id")
    )
    @Column(nullable=false)
    private List<Long> tarjetaIds;
}
