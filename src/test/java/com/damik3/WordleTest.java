package com.damik3;

import com.damik3.solver.heuristic.HeuristicSolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordleTest {

    @Test
    void Wordle_shouldParseWordsFile() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words_10.txt");
        List<String> words = wordle.validGuesses;
        List<String> possibleSolutions = wordle.possibleSolutions;
        assertEquals(10, words.size());
        assertEquals(7, possibleSolutions.size());
    }

    @Test
    void play_shouldSolveSlate() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new HeuristicSolver());
        Wordle.Result result = wordle.play("slate");
        assertTrue(result.solved);
    }

    @Test
    void play_shouldNotThrowWhenThereAreNoPossibleSolutions() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new HeuristicSolver());
        Wordle.Result result = wordle.play("zzzzz");
        assertFalse(result.solved);
    }

    //@Test
    //void play() throws IOException {
    //    Wordle wordle = new Wordle();
    //    wordle.loadWords("words.txt");
    //    wordle.setSolver(new WordleBotSolver());
    //    String guess;
    //    List<Guess> guessResult = new ArrayList<>();
    //
    //    guess = wordle.nextGuess(guessResult);
    //    System.out.println("Guess: " + guess);
    //    guessResult = new ArrayList<>(
    //        List.of(
    //            new Guess('s', 0, NotExists),
    //            new Guess('l', 1, NotExists),
    //            new Guess('a', 2, WrongPosition),
    //            new Guess('t', 3, WrongPosition),
    //            new Guess('e', 4, NotExists)
    //        )
    //    );
    //    guess = wordle.nextGuess(guessResult);
    //    System.out.println("Guess: " + guess);
    //}

}
