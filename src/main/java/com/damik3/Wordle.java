package com.damik3;

import com.damik3.model.Guess;
import com.damik3.model.Word;
import com.damik3.solver.Solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Wordle {

    List<String> words;
    List<String> possibleSolutions;
    String openingWord;
    Solver solver;

    public Wordle(String wordsFileName, Solver solver) throws IOException {
        List<Word> words = parseWords(wordsFileName);
        this.words = words
            .stream()
            .map(w -> w.word)
            .collect(Collectors.toList());
        this.possibleSolutions = words
            .stream()
            .filter(w -> w.prior > 0)
            .map(w -> w.word)
            .collect(Collectors.toList());
        this.openingWord = words
            .stream()
            .min(Comparator.comparingDouble(w -> w.expectedAdditionalGuesses))
            .get().word;
        this.solver = solver;
    }

    public String nextGuess(List<Guess> previousGuess) {
        if (previousGuess == null || previousGuess.isEmpty()) {
            return this.openingWord;
        }
        Rules.eliminateWords(possibleSolutions, previousGuess);
        System.out.println("Possible Solutions: " + possibleSolutions.size());
        return this.solver.nextGuess(this.words, this.possibleSolutions);
    }

    List<Word> parseWords(String filename) throws IOException {
        InputStream inputStream = getClass()
            .getClassLoader()
            .getResourceAsStream(filename);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader
                .lines()
                .skip(1) // Skip header
                .filter(line -> !line
                    .trim()
                    .isEmpty())
                .map(Word::new)
                .collect(Collectors.toList());
        }
    }

}
