package com.damik3;

import com.damik3.model.Guess;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordleBotTest {


    @Test
    void play() throws IOException {
        WordleBot bot = new WordleBot("words.txt");
        String guess;
        List<Guess> guessResult = new ArrayList<>();

        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('s', 0, Guess.Result.NotExists),
                new Guess('l', 1, Guess.Result.NotExists),
                new Guess('a', 2, Guess.Result.WrongPosition),
                new Guess('t', 3, Guess.Result.WrongPosition),
                new Guess('e', 4, Guess.Result.NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('t', 0, Guess.Result.WrongPosition),
                new Guess('a', 1, Guess.Result.CorrectPosition),
                new Guess('c', 2, Guess.Result.WrongPosition),
                new Guess('i', 3, Guess.Result.NotExists),
                new Guess('t', 4, Guess.Result.WrongPosition)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('w', 0, Guess.Result.NotExists),
                new Guess('o', 1, Guess.Result.NotExists),
                new Guess('m', 2, Guess.Result.NotExists),
                new Guess('b', 3, Guess.Result.WrongPosition),
                new Guess('s', 4, Guess.Result.NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);

    }

}
