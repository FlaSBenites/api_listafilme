package com.br.listafilme.application.controller;

import com.br.listafilme.domain.model.Movie;
import com.br.listafilme.domain.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Endpoint para listar todos os filmes
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    // Endpoint para obter detalhes de um filme específico
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.findById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/intervals")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getProducerAwardIntervals() {
        Map<String, List<Map<String, Object>>> intervals = movieService.calculateIntervals();
        return ResponseEntity.ok(intervals);
    }
}
