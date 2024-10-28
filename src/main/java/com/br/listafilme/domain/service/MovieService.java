package com.br.listafilme.domain.service;

import com.br.listafilme.domain.model.Movie;
import com.br.listafilme.infrastructure.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

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

        for (Map.Entry<String, List<Movie>> entry : moviesByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Movie> producerMovies = entry.getValue();

            // Ordenar os filmes do produtor por ano
            producerMovies.sort(Comparator.comparing(Movie::getReleaseYear));

            for (int i = 1; i < producerMovies.size(); i++) {
                int interval = producerMovies.get(i).getReleaseYear() - producerMovies.get(i - 1).getReleaseYear();
                int previousWin = producerMovies.get(i - 1).getReleaseYear();
                int followingWin = producerMovies.get(i).getReleaseYear();

                if (interval < minInterval) {
                    minInterval = interval;
                    minIntervals.clear();
                    minIntervals.add(createIntervalMap(producer, interval, previousWin, followingWin));
                } else if (interval == minInterval) {
                    minIntervals.add(createIntervalMap(producer, interval, previousWin, followingWin));
                }

                if (interval > maxInterval) {
                    maxInterval = interval;
                    maxIntervals.clear();
                    maxIntervals.add(createIntervalMap(producer, interval, previousWin, followingWin));
                } else if (interval == maxInterval) {
                    maxIntervals.add(createIntervalMap(producer, interval, previousWin, followingWin));
                }
            }
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("min", minIntervals);
        result.put("max", maxIntervals);
        return result;
    }

    private Map<String, Object> createIntervalMap(String producer, int interval, int previousWin, int followingWin) {
        Map<String, Object> intervalMap = new HashMap<>();
        intervalMap.put("producer", producer);
        intervalMap.put("interval", interval);
        intervalMap.put("previousWin", previousWin);
        intervalMap.put("followingWin", followingWin);
        return intervalMap;
    }
}
