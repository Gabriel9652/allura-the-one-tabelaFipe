package com.br.allura.tabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;


public record DadosVeiculos(String codigo,
                            String nome) {
}
