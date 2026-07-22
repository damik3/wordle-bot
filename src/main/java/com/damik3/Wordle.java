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

    static final int maxNumberOfGuesses = 6;

    List<Word> words;
    List<String> validGuesses;
    List<String> possibleSolutions;
    Solver solver;

    static public class Result {
        public final boolean solved;
        public final int steps;
        public final List<String> guesses;
        public final List<Integer> numPossibleSolutions;

        public Result(boolean solved, int steps, List<String> guesses, List<Integer> numPossibleSolutions) {
            this.solved = solved;
            this.steps = steps;
            this.guesses = guesses;
            this.numPossibleSolutions = numPossibleSolutions;
        }
    }

    public void loadWords(String wordsFileName) throws IOException {
        this.words = parse(wordsFileName);
        this.validGuesses = words
            .stream()
            .map(w -> w.word)
            .collect(Collectors.toList());
        this.possibleSolutions = words
            .stream()
            .filter(w -> w.prior > 0)
            .map(w -> w.word)
            .collect(Collectors.toList());
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public Result play(String solution) {
        int attempt = 0;
        String guess = null;
        List<String> guesses = new ArrayList<>();
        List<Guess> guessResult = new ArrayList<>();
        List<Integer> numPossibleSolutions = new ArrayList<>();

        while (attempt < maxNumberOfGuesses && !Objects.equals(guess, solution)) {
            guess = nextGuess(guessResult);
            guesses.add(guess);
            numPossibleSolutions.add(possibleSolutions.size());
            guessResult = Rules.calculateGuess(guess, solution);
            attempt++;
        }

        boolean solved = Objects.equals(guess, solution);
        return new Result(solved, attempt, guesses, numPossibleSolutions);
    }

    public String nextGuess(List<Guess> previousGuess) {
        if (previousGuess == null || previousGuess.isEmpty())
            return solver.firstGuess(words);
        Rules.eliminateWords(possibleSolutions, previousGuess);
        return solver.nextGuess(validGuesses, possibleSolutions);
    }

    private List<Word> parse(String filename) throws IOException {
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
