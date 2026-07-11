package com.damik3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class WordleBot {

    List<String> words;
    List<String> possibleSolutions;

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
    }

    public String nextGuess(List<Guess> previousGuess) {
        if (previousGuess == null || previousGuess.isEmpty()) {
            return "slate";
        }
        eliminateWords(previousGuess);
        Map<String, Map<List<GuessResult>, Integer>> patternCountsByWord = calculatePatternCounts();
        Map<String, Stats> statsByWord = calculateStatsByWord(patternCountsByWord);
        return calculateNextBestGuess(statsByWord);
    }

    String calculateNextBestGuess(Map<String, Stats> statsByWord) {
        List<Map.Entry<String, Stats>> entryList = new ArrayList<>(statsByWord.entrySet());
        entryList.sort(Comparator
            .comparingDouble((Map.Entry<String, Stats> e) -> e.getValue().score)
            .reversed());
        Map.Entry<String, Stats> bestGuess = entryList.get(0);
        return bestGuess.getKey();
    }

    Map<String, Stats> calculateStatsByWord(Map<String, Map<List<GuessResult>, Integer>> patternCountsByWord) {
        Map<String, Stats> statsByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Integer numberOfGroups = patternCounts.size();
            AtomicReference<Integer> largestGroup = new AtomicReference<>(0);
            patternCounts
                .values()
                .forEach(size -> {
                    if (size > largestGroup.get()) {
                        largestGroup.set(size);
                    }
                });
            Double isPossibleSolution = possibleSolutions.contains(s) ? 1.0 : 0;
            statsByWord.put(s, new Stats(numberOfGroups, largestGroup.get(), isPossibleSolution));
        });
        return statsByWord;
    }

    Map<String, Map<List<GuessResult>, Integer>> calculatePatternCounts() {
        Map<String, Map<List<GuessResult>, Integer>> patternCountsByWord = new HashMap<>();
        words.forEach(nextGuess -> {
            Map<List<GuessResult>, Integer> patternCounts = new HashMap<>();
            possibleSolutions.forEach(possibleSolution -> {
                List<GuessResult> guessResults = calculateGuess(nextGuess, possibleSolution)
                    .stream()
                    .map(g -> g.guessResult)
                    .collect(Collectors.toList());
                patternCounts.merge(guessResults, 1, Integer::sum);
            });
            patternCountsByWord.put(nextGuess, patternCounts);
        });
        return patternCountsByWord;
    }

    List<Guess> calculateGuess(String guessWord, String solutionWord) {
        assert guessWord.length() == solutionWord.length();
        List<Character> guess = Utils.stringToCharList(guessWord);
        List<Character> solution = Utils.stringToCharList(solutionWord);
        List<Guess> result = new ArrayList<>();
        for (int i = 0; i < solution.size(); i++) {
            Character solutionChar = solution.get(i);
            Character guessChar = guess.get(i);
            if (solutionChar.equals(guessChar)) {
                result.add(new Guess(guessChar, i, GuessResult.CorrectPosition));
            } else if (solution.contains(guessChar)) {
                result.add(new Guess(guessChar, i, GuessResult.WrongPosition));
            } else {
                result.add(new Guess(guessChar, i, GuessResult.NotExists));
            }
        }
        return result;
    }

    void eliminateWords(List<Guess> previousGuess) {
        words.removeIf(word -> isPreviousGuess(word, previousGuess));
        possibleSolutions.removeIf(word -> containsNonExistingLetters(word, previousGuess));
        possibleSolutions.removeIf(word -> !containsExistingLetters(word, previousGuess));
        possibleSolutions.removeIf(word -> !correctLettersAreInCorrectPosition(word, previousGuess));
        possibleSolutions.removeIf(word -> !existingLettersAreInADifferentPosition(word, previousGuess));
        System.out.println("Possible Solutions: " + possibleSolutions.size());
    }

    static boolean isPreviousGuess(String word, List<Guess> previousGuess) {
        String previousGuessWord = previousGuess
            .stream()
            .map(g -> g.letter)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
        return word.equals(previousGuessWord);
    }

    static boolean containsNonExistingLetters(String word, List<Guess> previousGuess) {
        Set<Integer> nonExistingLetters = previousGuess
            .stream()
            .filter(guess -> guess.guessResult == GuessResult.NotExists)
            .map(guess -> (int) guess.letter)
            .collect(Collectors.toSet());
        return word
            .chars()
            .anyMatch(nonExistingLetters::contains);
    }

    static boolean containsExistingLetters(String word, List<Guess> previousGuess) {
        Set<Integer> existingLetters = previousGuess
            .stream()
            .filter(
                guess -> guess.guessResult == GuessResult.CorrectPosition || guess.guessResult == GuessResult.WrongPosition)
            .map(guess -> (int) guess.letter)
            .collect(Collectors.toSet());
        return existingLetters
            .stream()
            .allMatch(l -> word.indexOf(l) != -1);
    }

    static boolean correctLettersAreInCorrectPosition(String word, List<Guess> previousGuess) {
        return previousGuess
            .stream()
            .filter(guess -> guess.guessResult == GuessResult.CorrectPosition)
            .allMatch(guess -> word.charAt(guess.index) == guess.letter);
    }

    static boolean existingLettersAreInADifferentPosition(String word, List<Guess> previousGuess) {
        return previousGuess
            .stream()
            .filter(guess -> guess.guessResult == GuessResult.WrongPosition)
            .allMatch(guess -> word.charAt(guess.index) != guess.letter && word.indexOf(guess.letter) != -1);
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
