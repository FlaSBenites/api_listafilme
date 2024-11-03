package com.br.listafilme.infrastructure.repository;

import com.br.listafilme.domain.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAll();

    Optional<Movie> findById(Long id);

    @Query("SELECT m FROM Movie m WHERE m.winner = true ORDER BY m.producer")
    List<Movie> findWinnersOrderedByProducer();

}
