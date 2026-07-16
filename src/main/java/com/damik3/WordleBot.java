package com.damik3;

import com.damik3.model.Guess;
import com.damik3.model.Stats;
import com.damik3.model.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordleBot {

    List<String> words;
    List<String> possibleSolutions;
    String openingWord;

    public WordleBot(String wordsFileName) throws IOException {
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
        this.openingWord = words.stream()
            .min(Comparator.comparingDouble(w -> w.expectedAdditionalGuesses))
            .get()
            .word;
    }

    public String nextGuess(List<Guess> previousGuess) {
        if (previousGuess == null || previousGuess.isEmpty()) {
            return this.openingWord;
        }
        eliminateWords(possibleSolutions, previousGuess);
        System.out.println("Possible Solutions: " + possibleSolutions.size());
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = calculatePatternCounts();
        Map<String, Stats> statsByWord = calculateStatsByWord(patternCountsByWord);
        return calculateNextBestGuess(statsByWord);
    }

    static String calculateNextBestGuess(Map<String, Stats> statsByWord) {
        List<Map.Entry<String, Stats>> entryList = new ArrayList<>(statsByWord.entrySet());
        Map.Entry<String, Stats> bestGuess = entryList.stream()
            .max(Comparator.comparingDouble((Map.Entry<String, Stats> e) -> e.getValue().score))
            .get();
        return bestGuess.getKey();
    }

    Map<String, Stats> calculateStatsByWord(Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord) {
        Map<String, Stats> statsByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Integer numberOfGroups = patternCounts.size();
            Integer largestGroup = patternCounts.values().stream().max(Comparator.comparingInt(i -> i)).get();
            Double isPossibleSolution = possibleSolutions.contains(s) ? 1.0 : 0;
            statsByWord.put(s, new Stats(numberOfGroups, largestGroup, isPossibleSolution));
        });
        return statsByWord;
    }

    Map<String, Map<List<Guess.Result>, Integer>> calculatePatternCounts() {
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = new HashMap<>();
        words.forEach(nextGuess -> {
            Map<List<Guess.Result>, Integer> patternCounts = new HashMap<>();
            possibleSolutions.forEach(possibleSolution -> {
                List<Guess.Result> guessResults = calculateGuess(nextGuess, possibleSolution)
                    .stream()
                    .map(g -> g.guessResult)
                    .collect(Collectors.toList());
                patternCounts.merge(guessResults, 1, Integer::sum);
            });
            patternCountsByWord.put(nextGuess, patternCounts);
        });
        return patternCountsByWord;
    }

    static void eliminateWords(List<String> possibleSolutions, List<Guess> previousGuess) {
        String previousGuessWord = previousGuess.stream()
            .map(g -> g.letter)
            .map(String::valueOf)
            .collect(Collectors.joining());
        possibleSolutions.removeIf(possibleSolution ->
            !previousGuess.equals(calculateGuess(previousGuessWord, possibleSolution))
        );
    }

    static List<Guess> calculateGuess(String guessWord, String solutionWord) {
        assert guessWord.length() == solutionWord.length();
        List<Character> guess = Utils.stringToCharList(guessWord);
        List<Character> solution = Utils.stringToCharList(solutionWord);
        Map<Character, Long> consumed = solutionWord
            .chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Guess> result = new ArrayList<>();
        for (int i = 0; i < solution.size(); i++) {
            Character solutionChar = solution.get(i);
            Character guessChar = guess.get(i);
            if (solutionChar.equals(guessChar)) {
                result.add(new Guess(guessChar, i, Guess.Result.CorrectPosition));
                consumed.put(guessChar, consumed.get(guessChar) - 1);
            } else if (solution.contains(guessChar) && consumed.get((guessChar)) > 0) {
                result.add(new Guess(guessChar, i, Guess.Result.WrongPosition));
                consumed.put(guessChar, consumed.get(guessChar) - 1);
            } else {
                result.add(new Guess(guessChar, i, Guess.Result.NotExists));
            }
        }
        return result;
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
                .filter(line -> !line.trim().isEmpty())
                .map(Word::new)
                .collect(Collectors.toList());
        }
    }

}
