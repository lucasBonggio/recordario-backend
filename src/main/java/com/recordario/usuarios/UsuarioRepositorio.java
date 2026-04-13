package com.recordario.usuarios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>{

    boolean existsByNombreUsuario(String nombreUsuario);

    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    @Query("SELECT COUNT(t) FROM Tarjeta t WHERE t.usuario.id = :usuarioId")
    int countTarjetasTotales(@Param("usuarioId") Long usuarioId);

        @Query("""
    SELECT COUNT(t)
    FROM Tarjeta t
    WHERE t.usuario.id = :usuarioId
    AND t.proximaRevision <= CURRENT_DATE
    AND (t.ultimaRevision IS NULL OR t.ultimaRevision <> CURRENT_DATE)
    """)
    int countTarjetasPendientes(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(t) FROM Tarjeta t WHERE t.usuario.id = :usuarioId AND t.ultimaRevision = CURRENT_DATE")
    int countTarjetasRepasadasHoy(@Param("usuarioId") Long usuarioId);
}
