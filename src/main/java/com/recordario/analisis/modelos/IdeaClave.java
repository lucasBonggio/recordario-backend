package com.recordario.analisis.modelos;


import com.recordario.compartido.enums.Estado;
import com.recordario.compartido.enums.TipoCarta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IdeaClave {
    private Long id;
    private String texto;
    private TipoCarta tipo;
    private Estado estado; 

    public IdeaClave(String texto){
        this.texto = texto;
    }
}
