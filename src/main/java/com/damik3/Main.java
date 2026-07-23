package com.damik3;

import com.damik3.solver.entropy.EntropySolver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new EntropySolver());

        Wordle.Result result = wordle.play("poppy");
        System.out.println(result.solved ? "Wordle at " + result.steps + "!" : "Could not find solution...");
        System.out.println("Guesses: " + result.guesses);
        System.out.println("Possible solutions: " + result.numPossibleSolutions);
    }
}
