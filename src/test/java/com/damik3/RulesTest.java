package com.damik3;

import com.damik3.model.Guess;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesTest {

    @Test
    void calculateGuess_shouldWork() {
        String guessWord = "abcde";
        String solutionWord = "bcffe";
        List<Guess> guesses = Rules.calculateGuess(guessWord, solutionWord);
        List<Guess> expectedGuesses = List.of(
            new Guess('a', 0, NotExists),
            new Guess('b', 1, WrongPosition),
            new Guess('c', 2, WrongPosition),
            new Guess('d', 3, NotExists),
            new Guess('e', 4, CorrectPosition)
        );
        assertEquals(expectedGuesses, guesses);
    }

    @Test
    void calculateGuess_shouldWorkForDoubleLetters_when1Exists_and2AreGuessed() {
        String guessWord1 = "aafff";
        String solutionWord1 = "abbbb";
        List<Guess> guesses1 = Rules.calculateGuess(guessWord1, solutionWord1);
        List<Guess> expectedGuesses1 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses1, guesses1);

        String guessWord2 = "fffaa";
        String solutionWord2 = "abbbb";
        List<Guess> guesses2 = Rules.calculateGuess(guessWord2, solutionWord2);
        List<Guess> expectedGuesses2 = List.of(
            new Guess('f', 0, NotExists),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('a', 3, WrongPosition),
            new Guess('a', 4, NotExists)
        );
        assertEquals(expectedGuesses2, guesses2);
    }

    @Test
    void calculateGuess_shouldWorkForDoubleLetters_when2Exist_and2AreGuessed() {
        String guessWord1 = "aafff";
        String solutionWord1 = "aabbb";
        List<Guess> guesses1 = Rules.calculateGuess(guessWord1, solutionWord1);
        List<Guess> expectedGuesses1 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, CorrectPosition),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses1, guesses1);

        String guessWord2 = "afaff";
        String solutionWord2 = "aabbb";
        List<Guess> guesses2 = Rules.calculateGuess(guessWord2, solutionWord2);
        List<Guess> expectedGuesses2 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('f', 1, NotExists),
            new Guess('a', 2, WrongPosition),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses2, guesses2);

        String guessWord3 = "fffaa";
        String solutionWord3 = "aabbb";
        List<Guess> guesses3 = Rules.calculateGuess(guessWord3, solutionWord3);
        List<Guess> expectedGuesses3 = List.of(
            new Guess('f', 0, NotExists),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('a', 3, WrongPosition),
            new Guess('a', 4, WrongPosition)
        );
        assertEquals(expectedGuesses3, guesses3);

    }

    @Test
    void calculateGuess_shouldWorkForDoubleLetters_when2Exist_and1IsGuessed() {
        String guessWord1 = "affff";
        String solutionWord1 = "aacde";
        List<Guess> guesses1 = Rules.calculateGuess(guessWord1, solutionWord1);
        List<Guess> expectedGuesses1 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses1, guesses1);

        String guessWord2 = "ffffa";
        String solutionWord2 = "aacde";
        List<Guess> guesses2 = Rules.calculateGuess(guessWord2, solutionWord2);
        List<Guess> expectedGuesses2 = List.of(
            new Guess('f', 0, NotExists),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('a', 4, WrongPosition)
        );
        assertEquals(expectedGuesses2, guesses2);
    }

    @Test
    void calculateGuess_shouldMarkCorrectFirstAndThenIfAnyMarkWrongPosition() {
        String guessWord = "aerie";
        String solutionWord = "raise";
        List<Guess> guesses = Rules.calculateGuess(guessWord, solutionWord);
        List<Guess> expectedGuesses = List.of(
            new Guess('a', 0, WrongPosition),
            new Guess('e', 1, NotExists),
            new Guess('r', 2, WrongPosition),
            new Guess('i', 3, WrongPosition),
            new Guess('e', 4, CorrectPosition)
        );
        assertEquals(expectedGuesses, guesses);
    }

}
