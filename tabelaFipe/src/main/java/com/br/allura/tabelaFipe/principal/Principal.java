package com.br.allura.tabelaFipe.principal;

import com.br.allura.tabelaFipe.model.DadosVeiculos;
import com.br.allura.tabelaFipe.model.Modelos;
import com.br.allura.tabelaFipe.model.Veiculo;
import com.br.allura.tabelaFipe.service.ConsumoAPI;
import com.br.allura.tabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu(){

        var menu = """
                ***OPÇÕES***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar
                """;

        System.out.println(menu);
        var opcao = leitura.nextLine();

        String endereco;

        if(opcao.toLowerCase().contains("carro")){
            endereco = URL_BASE+ "carros/marcas";
        } else if (opcao.toLowerCase().contains("moto")) {
            endereco = URL_BASE+ "motos/marcas";
        } else{
            endereco = URL_BASE+ "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);

        System.out.println(json);

        var marcas = conversor.obterLista(json,DadosVeiculos.class);

        marcas.stream()
                .sorted(Comparator.comparing(DadosVeiculos::codigo))
                .forEach(System.out::println);

        System.out.println("\nInforme o codigo da marca para consulta");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);

        var modelosLista = conversor.obterDados(json, Modelos.class);

        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculos::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado");
        var nomeVeiculo = leitura.nextLine();

        List<DadosVeiculos> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelo filtrados");
        modelosFiltrados.stream()
                .forEach(System.out::println);

        System.out.println("Digite o codigo do modelo para buscar os valores de avaliação");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);

        List<DadosVeiculos> anos = conversor.obterLista(json, DadosVeiculos.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++){
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos veiculos filtrados por ano:");

        veiculos.forEach(System.out::println);



















    }

}
