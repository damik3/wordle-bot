package com.damik3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        String wordle = "adieu";
        GameResult result = play(wordle);
        if (result.solved)
            System.out.println("Wordle at " + result.steps + "!");
        else
            System.out.println("Could not find solution...");
    }

    static GameResult play(String wordle) throws IOException {
        int maxNumberOfGuesses = 6;
        WordleBot bot = new WordleBot("words.txt");

        int attempt = 0;
        String guess = null;
        List<LetterGuess> previousGuessResult = new ArrayList<>();

        while (attempt < maxNumberOfGuesses && !Objects.equals(guess, wordle)) {
            guess = bot.nextGuess(previousGuessResult);
            System.out.println("Guess: " + guess);
            previousGuessResult = bot.calculateLetterGuess(guess, wordle);
            System.out.println("previousGuessResult: " + previousGuessResult);
            attempt++;
        }

        boolean solved = Objects.equals(guess, wordle);
        return new GameResult(solved, attempt);
    }
}
