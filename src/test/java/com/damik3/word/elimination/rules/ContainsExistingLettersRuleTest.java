package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainsExistingLettersRuleTest {

    @Test
    void containsExistingLetters_works() {
        WordEliminationRule rule = new ContainsExistingLettersRule();

        String word1 = "fffbc";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.CorrectPosition),
            new Guess('c', 2, Guess.Result.WrongPosition),
            new Guess('d', 3, Guess.Result.NotExists),
            new Guess('e', 4, Guess.Result.NotExists)
        );
        assertTrue(rule.apply(word1, previousGuess1));

        String word2 = "fffff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.CorrectPosition),
            new Guess('c', 2, Guess.Result.WrongPosition),
            new Guess('d', 3, Guess.Result.NotExists),
            new Guess('e', 4, Guess.Result.NotExists)
        );
        assertFalse(rule.apply(word2, previousGuess2));
    }

}
