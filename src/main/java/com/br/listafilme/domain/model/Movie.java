package com.br.listafilme.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean winner;
    @Column(name = "\"year\"")
    private int releaseYear;
    private String producer;
    private String title;

    public Movie(boolean winner, int releaseYear, String producer, String title) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.producer = producer;
        this.winner = winner;
    }

}
