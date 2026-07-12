package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;

import java.util.List;

public class CorrectLettersAreInCorrectPositionRule implements WordEliminationRule {
    @Override
    public boolean apply(String word, List<Guess> previousGuess) {
        return previousGuess
            .stream()
            .filter(guess -> guess.guessResult == Guess.Result.CorrectPosition)
            .allMatch(guess -> word.charAt(guess.index) == guess.letter);
    }
}
