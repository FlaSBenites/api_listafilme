package com.br.listafilme.infrastructure.repository;

import com.br.listafilme.domain.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByWinnerTrue();

    List<Movie> findAll();

    Optional<Movie> findById(Long id);

}
