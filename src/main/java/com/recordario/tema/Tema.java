package com.recordario.tema;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Tema {
    private Long id;
    private String titulo;
    private List<String> ideasPrincipales = new ArrayList<>();
}