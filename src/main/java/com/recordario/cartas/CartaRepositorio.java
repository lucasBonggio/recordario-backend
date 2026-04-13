package com.recordario.cartas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaRepositorio extends JpaRepository<Carta, Long>{

}
