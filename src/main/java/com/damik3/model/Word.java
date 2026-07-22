package com.damik3.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Word {
    public static class Tuple<T> {
        public final T first;
        public final T second;

        public Tuple(T first, T second) {
            this.first = first;
            this.second = second;
        }
    }

    public String word;
    public int level;
    public List<Double> entropyVec;
    public List<Integer> percentileVec;
    public Double expectedEntropy;
    public Double expectedWordsRemaining;
    public Integer maxWordsRemaining;
    public Integer numberOfGroups;
    public Double prior;
    public Tuple<Double> precomputedAverage;
    public Double expectedAdditionalGuesses;

    // Constructor that accepts a raw line from words.txt
    public Word(String line) {
        if (line == null || line
            .trim()
            .isEmpty()) {
            throw new IllegalArgumentException("Line is empty or is a header row");
        }

        String[] columns = line
            .trim()
            .split("\\s+");

        if (columns.length < 11) {
            throw new IllegalArgumentException("Incomplete data line. Expected 11 columns, got " + columns.length);
        }

        this.word = columns[0];
        this.level = Integer.parseInt(columns[1]);
        this.entropyVec = Arrays
            .stream(columns[2].split(","))
            .map(Double::parseDouble)
            .collect(Collectors.toList());
        this.percentileVec = Arrays
            .stream(columns[3].split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        this.expectedEntropy = Double.parseDouble(columns[4]);
        this.expectedWordsRemaining = Double.parseDouble(columns[5]);
        this.maxWordsRemaining = Integer.parseInt(columns[6]);
        this.numberOfGroups = Integer.parseInt(columns[7]);
        this.prior = Double.parseDouble(columns[8]);
        this.precomputedAverage = new Tuple<>(Double.parseDouble(columns[9].split(",")[0]),
            Double.parseDouble(columns[9].split(",")[1]));
        this.expectedAdditionalGuesses = Double.parseDouble(columns[10]);
    }
}
