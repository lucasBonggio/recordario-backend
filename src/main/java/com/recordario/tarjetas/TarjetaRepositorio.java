package com.recordario.tarjetas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recordario.cartas.Carta;
import com.recordario.usuarios.Usuario;

@Repository
public interface TarjetaRepositorio extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findAllByUsuario(Usuario usuario);
    boolean existsByUsuarioIdAndCarta(Usuario usuario, Carta carta);
}
