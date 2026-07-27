package com.damik3;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordleTest {

    @Test
    void Wordle_shouldParseWordsFile() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words_5.txt");
        List<String> words = wordle.validGuesses;
        List<String> possibleSolutions = wordle.possibleSolutions;
        assertEquals(5, words.size());
        assertEquals(3, possibleSolutions.size());
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
