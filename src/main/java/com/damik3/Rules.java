package com.damik3;

import com.damik3.model.Guess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Rules {

    static void eliminateWords(List<String> possibleSolutions, List<Guess> previousGuess) {
        if (previousGuess == null || previousGuess.isEmpty())
            return;
        String previousGuessWord = previousGuess
            .stream()
            .sorted(Comparator.comparingInt(g -> g.index))
            .map(g -> g.letter)
            .map(String::valueOf)
            .collect(Collectors.joining());
        possibleSolutions.removeIf(
            possibleSolution -> !previousGuess.equals(calculateGuess(previousGuessWord, possibleSolution)));
    }

    static public List<Guess> calculateGuess(String guessWord, String solutionWord) {
        if (guessWord == null) {
            throw new IllegalArgumentException("guessWord is null");
        }

        if (solutionWord == null) {
            throw new IllegalArgumentException("solutionWord is null");
        }

        int[] consumed = new int[26];
        for (int i = 0; i < solutionWord.length(); i++) {
            consumed[solutionWord.charAt(i) - 'a']++;
        }
        List<Guess> result = new ArrayList<>();

        // Consume correct letters first
        for (int i = 0; i < solutionWord.length(); i++) {
            char solutionChar = solutionWord.charAt(i);
            char guessChar = guessWord.charAt(i);
            if (solutionChar == guessChar) {
                result.add(new Guess(guessChar, i, Guess.Result.CorrectPosition));
                consumed[guessChar - 'a']--;
            }
        }

        // Consume wrong position and not existing letters second
        for (int i = 0; i < solutionWord.length(); i++) {
            char solutionChar = solutionWord.charAt(i);
            char guessChar = guessWord.charAt(i);
            if (solutionChar == guessChar) {
                continue;
            } else if (consumed[guessChar - 'a'] > 0) {
                result.add(new Guess(guessChar, i, Guess.Result.WrongPosition));
                consumed[guessChar - 'a']--;
            } else {
                result.add(new Guess(guessChar, i, Guess.Result.NotExists));
            }
        }

        result.sort(Comparator.comparing(g -> g.index));
        return result;
    }

}
