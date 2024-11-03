package com.br.listafilme.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProducerIntervalResponse {
    private List<Interval> min;
    private List<Interval> max;

    @Data
    @AllArgsConstructor
    public static class Interval {
        private String producer;
        private int interval;
        private int previousWin;
        private int followingWin;
    }
}
