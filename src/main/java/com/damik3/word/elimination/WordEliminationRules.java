package com.damik3.word.elimination;

import com.damik3.model.Guess;
import com.damik3.word.elimination.rules.*;

import java.util.List;

public class WordEliminationRules {

    public static void apply(List<String> possibleSolutions, List<Guess> previousGuess) {
        List<WordEliminationRule> rules = List.of(
            new ContainsNonExistingLettersRule(),
            new Not(new ContainsExistingLettersRule()),
            new Not(new CorrectLettersAreInCorrectPositionRule()),
            new Not(new ExistingLettersAreInADifferentPositionRule())
        );

        rules.forEach(rule ->
            possibleSolutions.removeIf(word ->
                rule.apply(word, previousGuess)
            )
        );
    }

}
