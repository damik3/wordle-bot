package com.damik3;

import com.damik3.model.Guess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

        Map<Character, Long> consumed = solutionWord
            .chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Guess> result = new ArrayList<>();

        // Consume correct letters first
        for (int i = 0; i < solutionWord.length(); i++) {
            Character solutionChar = solutionWord.charAt(i);
            Character guessChar = guessWord.charAt(i);
            if (solutionChar.equals(guessChar)) {
                result.add(new Guess(guessChar, i, Guess.Result.CorrectPosition));
                consumed.put(guessChar, consumed.get(guessChar) - 1);
            }
        }

        // Consume wrong position and not existing letters second
        for (int i = 0; i < solutionWord.length(); i++) {
            Character solutionChar = solutionWord.charAt(i);
            Character guessChar = guessWord.charAt(i);
            if (solutionChar.equals(guessChar)) {
                continue;
            } else if (solutionWord.indexOf(guessChar) != -1 && consumed.get(guessChar) > 0) {
                result.add(new Guess(guessChar, i, Guess.Result.WrongPosition));
                consumed.put(guessChar, consumed.get(guessChar) - 1);
            } else {
                result.add(new Guess(guessChar, i, Guess.Result.NotExists));
            }
        }

        result.sort(Comparator.comparing(g -> g.index));
        return result;
    }

}
