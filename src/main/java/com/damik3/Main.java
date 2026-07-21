package com.damik3;

import com.damik3.model.Guess;
import com.damik3.solver.Solver;
import com.damik3.solver.wordlebot.WordleBotSolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        String wordle = "pizza";
        GameResult gameResult = play(wordle);
        if (gameResult.solved)
            System.out.println("Wordle at " + gameResult.steps + "!");
        else
            System.out.println("Could not find solution...");
    }

    static GameResult play(String wordle) throws IOException {
        int maxNumberOfGuesses = 6;
        Solver solver = new WordleBotSolver();
        Wordle bot = new Wordle("words.txt", solver);

        int attempt = 0;
        String guess = null;
        List<Guess> guessResult = new ArrayList<>();

        while (attempt < maxNumberOfGuesses && !Objects.equals(guess, wordle)) {
            guess = bot.nextGuess(guessResult);
            System.out.println("\nGuess: " + guess);
            guessResult = Rules.calculateGuess(guess, wordle);
            System.out.println("Result: " + guessResult);
            attempt++;
        }

        boolean solved = Objects.equals(guess, wordle);
        return new GameResult(solved, attempt);
    }

    static public class GameResult {
        public final boolean solved;
        public final int steps;

        public GameResult(boolean solved, int steps) {
            this.solved = solved;
            this.steps = steps;
        }
    }
}
