package com.damik3.solver.heuristic;

import com.damik3.Wordle;
import com.damik3.model.Guess;
import com.damik3.solver.pattern.heuristic.HeuristicSolver;
import com.damik3.solver.pattern.heuristic.model.Stats;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeuristicSolverTest {

    @Test
    void shouldSolveSlate() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new HeuristicSolver());
        Wordle.Result result = wordle.play("slate");
        assertTrue(result.solved);
    }

    @Test
    void shouldNotThrowWhenThereAreNoPossibleSolutions() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new HeuristicSolver());
        Wordle.Result result = wordle.play("zzzzz");
        assertFalse(result.solved);
    }

    @Test
    void calculateScoreByWord_shouldWork() {
        HeuristicSolver heuristicSolver = new HeuristicSolver();

        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = Map.ofEntries(
            entry("raise", Map.ofEntries(
                entry(List.of(CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition), 1),
                entry(List.of(NotExists, WrongPosition, NotExists, WrongPosition, CorrectPosition), 1),
                entry(List.of(WrongPosition, CorrectPosition, NotExists, CorrectPosition, CorrectPosition), 1)
            )),
            entry("slate", Map.ofEntries(
                entry(List.of(WrongPosition, NotExists, WrongPosition, NotExists, CorrectPosition), 2),
                entry(List.of(CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition), 1)
            )),
            entry("parse", Map.ofEntries(
                entry(List.of(NotExists, CorrectPosition, WrongPosition, CorrectPosition, CorrectPosition), 1),
                entry(List.of(NotExists, WrongPosition, NotExists, WrongPosition, CorrectPosition), 1),
                entry(List.of(CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition), 1)
            )),
            entry("bubby", Map.ofEntries(
                entry(List.of(NotExists, NotExists, NotExists, NotExists, NotExists), 3)
            )),
            entry("yippy", Map.ofEntries(
                entry(List.of(NotExists, WrongPosition, NotExists, NotExists, NotExists), 1),
                entry(List.of(NotExists, NotExists, NotExists, NotExists, NotExists), 1),
                entry(List.of(NotExists, NotExists, WrongPosition, NotExists, NotExists), 1)
            ))
        );

        HashSet<String> possibleSolutions = new HashSet<>(List.of("raise", "slate", "parse"));
        Map<String, Double> scoreByWord = heuristicSolver.calculateScoreByWord(possibleSolutions, patternCountsByWord);

        Map<String, Double> expectedScoreByWord = Map.ofEntries(
            entry("raise", new Stats(3, 1, 1.0).score),
            entry("slate", new Stats(2, 2, 1.0).score),
            entry("parse", new Stats(3, 1, 1.0).score),
            entry("bubby", new Stats(1, 3, 0.0).score),
            entry("yippy", new Stats(3, 1, 0.0).score)
        );

        assertEquals(expectedScoreByWord, scoreByWord);
    }

}
