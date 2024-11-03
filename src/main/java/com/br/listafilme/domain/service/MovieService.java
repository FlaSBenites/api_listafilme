package com.br.listafilme.domain.service;

import com.br.listafilme.domain.model.Movie;
import com.br.listafilme.infrastructure.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Map<String, List<Map<String, Object>>> calculateIntervals() {
        List<Movie> winners = movieRepository.findByWinnerTrue();

        // Agrupar por produtor e ordenar filmes vencedores por ano
        Map<String, List<Movie>> moviesByProducer = winners.stream()
                .collect(Collectors.groupingBy(Movie::getProducer, Collectors.toList()));

        List<Map<String, Object>> minIntervals = new ArrayList<>();
        List<Map<String, Object>> maxIntervals = new ArrayList<>();
        int minInterval = Integer.MAX_VALUE;
        int maxInterval = Integer.MIN_VALUE;

        // Verificar se algum produtor tem filmes vencedores
        for (Map.Entry<String, List<Movie>> entry : moviesByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Movie> producerMovies = entry.getValue();

            // Ordenar os filmes do produtor por ano
            producerMovies.sort(Comparator.comparing(Movie::getReleaseYear));

            // Verifica se há pelo menos dois filmes para calcular o intervalo
            if (producerMovies.size() < 2) {
                logger.info("Producer " + producer + " has less than 2 winning movies. Skipping.");
                continue; // Pula produtores sem filmes suficientes
            }

            // Inicializa variáveis para os intervalos mínimo e máximo
            int previousWin = producerMovies.get(0).getReleaseYear();

            for (int i = 1; i < producerMovies.size(); i++) {
                int currentWin = producerMovies.get(i).getReleaseYear();
                int interval = currentWin - previousWin;

                // Atualiza mínimo
                if (interval < minInterval) {
                    minInterval = interval;
                    minIntervals.clear();
                    minIntervals.add(createIntervalMap(producer, interval, previousWin, currentWin));
                } else if (interval == minInterval) {
                    minIntervals.add(createIntervalMap(producer, interval, previousWin, currentWin));
                }

                // Atualiza máximo
                if (interval > maxInterval) {
                    maxInterval = interval;
                    maxIntervals.clear();
                    maxIntervals.add(createIntervalMap(producer, interval, previousWin, currentWin));
                } else if (interval == maxInterval) {
                    maxIntervals.add(createIntervalMap(producer, interval, previousWin, currentWin));
                }

                // Atualiza o ano anterior para o próximo cálculo
                previousWin = currentWin;
            }
        }

        // Exibir intervalos mínimos e máximos encontrados para depuração
        logger.info("Minimum Intervals: " + minIntervals);
        logger.info("Maximum Intervals: " + maxIntervals);

        // Verifica se houve alteração nos valores
        if (minIntervals.isEmpty()) {
            minIntervals.add(createIntervalMap("N/A", -1, -1, -1)); // Placeholder se não houver mínimo
        }
        if (maxIntervals.isEmpty()) {
            maxIntervals.add(createIntervalMap("N/A", -1, -1, -1)); // Placeholder se não houver máximo
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("min", minIntervals);
        result.put("max", maxIntervals);
        return result;
    }

    private Map<String, Object> createIntervalMap(String producer, int interval, int previousWin, int followingWin) {
        Map<String, Object> intervalMap = new LinkedHashMap<>();
        intervalMap.put("producer", producer);
        intervalMap.put("interval", interval);
        intervalMap.put("previousWin", previousWin);
        intervalMap.put("followingWin", followingWin);
        return intervalMap;
    }
}
