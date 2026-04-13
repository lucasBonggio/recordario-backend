package com.recordario.repaso;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.recordario.tarjetas.Tarjeta;

@Service
public class OrdenarPrioridad {

    public List<Tarjeta> ordenarTarjetas(List<Tarjeta> tarjetas){
        if(tarjetas.isEmpty()) return tarjetas;

        return tarjetas.stream()
                .sorted(Comparator.comparing(this::prioridadVencimiento))
                .collect(Collectors.toList());
        }

    public int prioridadVencimiento(Tarjeta tarjeta){
        if(tarjeta.getProximaRevision().isAfter(LocalDate.now())){
            return 4;
        }

        long dias = diasVencidos(tarjeta);
        if(dias >= 15) return 1;
        if(dias >= 7) return 2;
        return 3;
    }

    public long diasVencidos(Tarjeta t){
        long diferencia = ChronoUnit.DAYS.between(t.getProximaRevision(), LocalDate.now());
        return diferencia;
    }
}
