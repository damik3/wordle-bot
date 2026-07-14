package com.damik3;

import com.damik3.model.Game;
import com.damik3.model.Guess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        String wordle = "slain";
        Game game = play(wordle);
        if (game.solved)
            System.out.println("Wordle at " + game.steps + "!");
        else
            System.out.println("Could not find solution...");
    }

    static Game play(String wordle) throws IOException {
        int maxNumberOfGuesses = 6;
        WordleBot bot = new WordleBot("words.txt");

        int attempt = 0;
        String guess = null;
        List<Guess> guessResult = new ArrayList<>();

        while (attempt < maxNumberOfGuesses && !Objects.equals(guess, wordle)) {
            guess = bot.nextGuess(guessResult);
            System.out.println("\nGuess: " + guess);
            guessResult = WordleBot.calculateGuess(guess, wordle);
            System.out.println("Result: " + guessResult);
            attempt++;
        }

        boolean solved = Objects.equals(guess, wordle);
        return new Game(solved, attempt);
    }
}
