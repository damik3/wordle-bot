package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ContainsExistingLettersRule implements WordEliminationRule {
    @Override
    public boolean apply(String word, List<Guess> previousGuess) {
        Set<Integer> existingLetters = previousGuess
            .stream()
            .filter(
                guess -> guess.guessResult == Guess.Result.CorrectPosition || guess.guessResult == Guess.Result.WrongPosition)
            .map(guess -> (int) guess.letter)
            .collect(Collectors.toSet());
        return existingLetters
            .stream()
            .allMatch(l -> word.indexOf(l) != -1);
    }
}
