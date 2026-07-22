package com.damik3;

import com.damik3.solver.wordlebot.WordleBotSolver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new WordleBotSolver());

        Wordle.Result result = wordle.play("orbit");
        System.out.println(result.solved ? "Wordle at " + result.steps + "!" : "Could not find solution...");
        System.out.println("Guesses: " + result.guesses);
        System.out.println("Possible solutions: " + result.numPossibleSolutions);
    }
}
