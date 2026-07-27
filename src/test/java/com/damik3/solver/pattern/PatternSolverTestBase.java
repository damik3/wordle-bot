package com.damik3.solver.pattern;

import com.damik3.Wordle;
import com.damik3.solver.Solver;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class PatternSolverTestBase {
    protected abstract Solver newSolver();

    @Test
    void shouldSolveSlate() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(newSolver());
        Wordle.Result result = wordle.play("slate");
        assertTrue(result.solved);
    }

    @Test
    void shouldSolveCrony() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(newSolver());
        Wordle.Result result = wordle.play("crony");
        assertTrue(result.solved);
    }

    @Test
    void shouldNotThrowWhenThereAreNoPossibleSolutions() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(newSolver());
        Wordle.Result result = wordle.play("zzzzz");
        assertFalse(result.solved);
    }

}
