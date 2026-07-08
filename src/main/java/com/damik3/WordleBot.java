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
        List<String> parsedWords = parseWordsFile(wordsFileName);
        this.words = new ArrayList<>(parsedWords);
        this.possibleSolutions = new ArrayList<>(parsedWords);
    }

    /*
     * 1. Eliminate words based on guess result
     * 2. For each remaining guess
     *      For each possible solution
     *          Calculate pattern
     *    guess -> groups
     *    Best (next) guess is the one with smallest numGroups and largest groupSize
     * */
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
                List<GuessResult> guessResults = calculateGuessResult(nextGuess, possibleSolution);
                patternCounts.merge(guessResults, 1, Integer::sum);
            });
            patternCountsByWord.put(nextGuess, patternCounts);
        });
        return patternCountsByWord;
    }

    List<Guess> calculateGuess(String guessWord, String solutionWord) {
        List<GuessResult> guessResults = calculateGuessResult(guessWord, solutionWord);
        List<Guess> guesses = new ArrayList<>();
        for (int i = 0; i < guessResults.size(); i++) {
            GuessResult guessResult = guessResults.get(i);
            Character c = guessWord.charAt(i);
            guesses.add(new Guess(c, i, guessResult));
        }
        return guesses;
    }

    List<GuessResult> calculateGuessResult(String guessWord, String solutionWord) {
        assert guessWord.length() == solutionWord.length();
        List<Character> guess = Utils.stringToCharList(guessWord);
        List<Character> solution = Utils.stringToCharList(solutionWord);
        List<GuessResult> result = new ArrayList<>();
        for (int i = 0; i < solution.size(); i++) {
            Character solutionChar = solution.get(i);
            Character guessChar = guess.get(i);
            if (solutionChar.equals(guessChar)) {
                result.add(GuessResult.CorrectPosition);
            } else if (solution.contains(guessChar)) {
                result.add(GuessResult.WrongPosition);
            } else {
                result.add(GuessResult.NotExists);
            }
        }
        return result;
    }

    /*
     * Remove word if
     *   1. is previous guess
     *   2. it contains non-existing letters
     *   3. it does not contain existing letters
     * */
    void eliminateWords(List<Guess> previousGuess) {
        possibleSolutions.removeIf(word -> isPreviousGuess(word, previousGuess));

        words.removeIf(word -> isPreviousGuess(word, previousGuess));

        possibleSolutions.removeIf(word -> containsNonExistingLetters(word, previousGuess));

        possibleSolutions.removeIf(word -> !containsExistingLetters(word, previousGuess));

        possibleSolutions.removeIf(word -> correctLettersAreNotInCorrectPosition(word, previousGuess));

        possibleSolutions.removeIf(word -> existingLettersAreInWrongPosition(word, previousGuess));

        System.out.println("Possible Solutions: " + possibleSolutions.size());
    }

    boolean isPreviousGuess(String word, List<Guess> previousGuess) {
        String previousGuessWord = previousGuess
            .stream()
            .map(g -> g.letter)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
        return word.equals(previousGuessWord);
    }

    boolean containsNonExistingLetters(String word, List<Guess> previousGuess) {
        Set<Integer> nonExistingLetters = previousGuess
            .stream()
            .filter(guess -> guess.guessResult == GuessResult.NotExists)
            .map(guess -> (int) guess.letter)
            .collect(Collectors.toSet());
        return word
            .chars()
            .anyMatch(nonExistingLetters::contains);
    }

    boolean containsExistingLetters(String word, List<Guess> previousGuess) {
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

    boolean correctLettersAreNotInCorrectPosition(String word, List<Guess> previousGuess) {
        return !previousGuess
            .stream()
            .filter(guess -> guess.guessResult == GuessResult.CorrectPosition)
            .allMatch(guess -> word.charAt(guess.index) == guess.letter);
    }

    boolean existingLettersAreInWrongPosition(String word, List<Guess> previousGuess) {
        return !previousGuess
            .stream()
            .filter(guess -> guess.guessResult == GuessResult.WrongPosition)
            .allMatch(guess -> word.charAt(guess.index) != guess.letter);
    }

    List<String> parseWordsFile(String filename) throws IOException {
        InputStream inputStream = getClass()
            .getClassLoader()
            .getResourceAsStream(filename);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line
                    .trim()
                    .toLowerCase();
                if (!word.isEmpty())
                    words.add(word);
            }
        }
        return words;
    }

}
