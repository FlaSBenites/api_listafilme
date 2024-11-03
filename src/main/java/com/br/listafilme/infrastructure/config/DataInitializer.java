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
import java.util.*;
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
            logger.info("Carregando arquivo CSV: " + resource.exists());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                List<Movie> movies = reader.lines()
                        .skip(1) // Ignora o cabeçalho
                        .map(line -> {
                            String[] columns = line.split("[;,]");

                            if (columns.length < 5) {
                                logger.info("Linha inválida: " + line);
                                return null;
                            }

                            try {
                                boolean isWinner = columns[4].trim().equalsIgnoreCase("yes");
                                return new Movie(
                                        isWinner,
                                        Integer.parseInt(columns[0].trim()),
                                        columns[3].trim(),
                                        columns[1].trim()
                                );
                            } catch (NumberFormatException e) {
                               logger.error("Erro ao converter valores da linha: " + line + " - " + e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                logger.info("Total de filmes válidos lidos: " + movies.size());

                if (!movies.isEmpty()) {
                    movieRepository.saveAll(movies);
                    logger.info("Filmes salvos com sucesso na base de dados.");
                } else {
                     logger.info("Nenhum filme válido para salvar.");
                }
            }

        } catch (Exception e) {
            logger.error("ERROR: "+ e.getMessage());
        }
    }
}
