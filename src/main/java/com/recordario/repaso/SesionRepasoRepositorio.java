package com.recordario.repaso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionRepasoRepositorio extends JpaRepository<SesionRepasoEntidad, String>{
}
