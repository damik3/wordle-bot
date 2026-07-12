package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExistingLettersAreInADifferentPositionRuleTest {

    @Test
    void existingLettersAreInADifferentPosition_works() {
        WordEliminationRule rule = new ExistingLettersAreInADifferentPositionRule();

        String word1 = "ffffe";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.NotExists),
            new Guess('c', 2, Guess.Result.NotExists),
            new Guess('d', 3, Guess.Result.NotExists),
            new Guess('e', 4, Guess.Result.WrongPosition)
        );
        assertFalse(rule.apply(word1, previousGuess1));

        String word2 = "fffef";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.NotExists),
            new Guess('c', 2, Guess.Result.NotExists),
            new Guess('d', 3, Guess.Result.NotExists),
            new Guess('e', 4, Guess.Result.WrongPosition)
        );
        assertTrue(rule.apply(word2, previousGuess2));

        String word3 = "fffff";
        List<Guess> previousGuess3 = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.NotExists),
            new Guess('c', 2, Guess.Result.NotExists),
            new Guess('d', 3, Guess.Result.NotExists),
            new Guess('e', 4, Guess.Result.WrongPosition)
        );
        assertFalse(rule.apply(word3, previousGuess3));
    }

}
