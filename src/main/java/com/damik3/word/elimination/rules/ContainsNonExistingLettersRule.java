package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ContainsNonExistingLettersRule implements WordEliminationRule {
    @Override
    public boolean apply(String word, List<Guess> previousGuess) {
        Set<Integer> nonExistingLetters = previousGuess.stream()
            .filter(guess -> guess.guessResult == Guess.Result.NotExists)
            .map(guess -> (int) guess.letter)
            .collect(Collectors.toSet());

        return word.chars().anyMatch(nonExistingLetters::contains);
    }
}
