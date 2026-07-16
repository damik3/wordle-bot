package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainsNonExistingLettersRuleTest {

    WordEliminationRule rule = new ContainsNonExistingLettersRule();

    @Test
    void returnTrue_WhenWordContainsNonExistingLetters() {
        String word = "ffaff";
        List<Guess> previousGuess = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.CorrectPosition),
            new Guess('c', 2, Guess.Result.WrongPosition),
            new Guess('d', 3, Guess.Result.WrongPosition),
            new Guess('e', 4, Guess.Result.WrongPosition)
        );
        assertTrue(rule.apply(word, previousGuess));
    }

    @Test
    void returnFalse_WhenWordDoesNotContainNonExistingLetters() {
        String word = "fffff";
        List<Guess> previousGuess = List.of(
            new Guess('a', 0, Guess.Result.NotExists),
            new Guess('b', 1, Guess.Result.CorrectPosition),
            new Guess('c', 2, Guess.Result.WrongPosition),
            new Guess('d', 3, Guess.Result.WrongPosition),
            new Guess('e', 4, Guess.Result.WrongPosition)
        );
        assertFalse(rule.apply(word, previousGuess));
    }

    @Test
    void returnTrue_WhenWordContainsTwiceALetterThatShouldExistOnlyOnce() {
        String word = "aafff";
        List<Guess> previousGuess = List.of(
            new Guess('a', 0, Guess.Result.CorrectPosition),
            new Guess('b', 1, Guess.Result.NotExists),
            new Guess('a', 2, Guess.Result.NotExists),
            new Guess('c', 3, Guess.Result.WrongPosition),
            new Guess('d', 4, Guess.Result.WrongPosition)
        );
        assertTrue(rule.apply(word, previousGuess));
    }

    @Test
    void returnFalse_WhenWordContainsOnceALetterThatShouldExistOnlyOnceButWasGuessedTwice() {
        String word = "affff";
        List<Guess> previousGuess = List.of(
            new Guess('a', 0, Guess.Result.CorrectPosition),
            new Guess('b', 1, Guess.Result.NotExists),
            new Guess('a', 2, Guess.Result.NotExists),
            new Guess('c', 3, Guess.Result.WrongPosition),
            new Guess('d', 4, Guess.Result.WrongPosition)
        );
        assertFalse(rule.apply(word, previousGuess));
    }

    @Test
    void returnFalse_WhenWordContainsTwiceALetterThatMightExistOnceOrMore() {
        String word = "aafff";
        List<Guess> previousGuess = List.of(
            new Guess('a', 0, Guess.Result.CorrectPosition),
            new Guess('b', 1, Guess.Result.NotExists),
            new Guess('c', 2, Guess.Result.NotExists),
            new Guess('d', 3, Guess.Result.WrongPosition),
            new Guess('e', 4, Guess.Result.WrongPosition)
        );
        assertFalse(rule.apply(word, previousGuess));
    }

}
