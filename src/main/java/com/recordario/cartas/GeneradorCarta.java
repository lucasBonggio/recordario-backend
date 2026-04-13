package com.recordario.cartas;

import java.util.List;

public interface GeneradorCarta {
    List<Carta> generarCarta(String texto, Long idNota);
}
