package com.br.listafilme.infrastructure.config;


import com.br.listafilme.domain.model.Movie;
import com.br.listafilme.infrastructure.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/movielist.csv");
            System.out.println("Carregando arquivo CSV: " + resource.exists()); // Confirma que o arquivo existe

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                List<Movie> movies = reader.lines()
                        .skip(1) // Ignora o cabeçalho
                        .map(line -> {
                            String[] columns = line.split(";");

                            // Verifica se há colunas suficientes
                            if (columns.length < 5) {
                                System.err.println("Linha inválida: " + line);
                                return null;
                            }

                            try {
                                // Interpreta "yes" como true para o campo winner
                                boolean isWinner = columns[4].trim().equalsIgnoreCase("yes");

                                // Cria uma nova instância de Movie
                                Movie movie = new Movie(
                                        isWinner, // winner
                                        Integer.parseInt(columns[0].trim()), // year
                                        columns[3].trim(), // producer
                                        columns[1].trim() // title
                                );
                                System.out.println("Lendo filme: " + movie);
                                return movie;
                            } catch (NumberFormatException e) {
                                System.err.println("Erro ao converter valores da linha: " + line + " - " + e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull) // Filtra nulos
                        .collect(Collectors.toList());

                // Exibir o total de filmes válidos lidos para conferir o resultado da leitura
                System.out.println("Total de filmes válidos lidos: " + movies.size());

                // Salva todos os filmes na base de dados
                if (!movies.isEmpty()) {
                    movieRepository.saveAll(movies);
                    System.out.println("Filmes salvos com sucesso na base de dados.");
                } else {
                    System.out.println("Nenhum filme válido para salvar.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
