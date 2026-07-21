package com.damik3;

import com.damik3.model.Guess;
import com.damik3.solver.Solver;
import com.damik3.solver.wordlebot.WordleBotSolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordleTest {

    @Test
    void Wordle_shouldParseWordsFile() throws IOException {
        Solver solver = new WordleBotSolver();
        Wordle wordle = new Wordle("words_10.txt", solver);
        List<String> words = wordle.words;
        List<String> possibleSolutions = wordle.possibleSolutions;
        String openingWord = wordle.openingWord;
        assertEquals(10, words.size());
        assertEquals(7, possibleSolutions.size());
        assertEquals("slate", openingWord);
    }

    @Test
    void play() throws IOException {
        Solver solver = new WordleBotSolver();
        Wordle bot = new Wordle("words.txt", solver);
        String guess;
        List<Guess> guessResult = new ArrayList<>();

        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('s', 0, NotExists),
                new Guess('l', 1, NotExists),
                new Guess('a', 2, WrongPosition),
                new Guess('t', 3, WrongPosition),
                new Guess('e', 4, NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
    }

}
