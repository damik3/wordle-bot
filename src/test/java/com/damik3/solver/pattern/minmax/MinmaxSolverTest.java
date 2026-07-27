package com.damik3.solver.pattern.minmax;

import com.damik3.model.Guess;
import com.damik3.solver.Solver;
import com.damik3.solver.pattern.PatternSolverTestBase;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinmaxSolverTest extends PatternSolverTestBase {

    @Override
    protected Solver newSolver() {
        return new MinmaxSolver();
    }

    @Test
    void calculateScoreByWord_shouldWork() {
        MinmaxSolver minmaxSolver = new MinmaxSolver();

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

        List<String> possibleSolutions = List.of("raise", "slate", "parse");
        Map<String, Double> scoreByWord = minmaxSolver.calculateScoreByWord(new HashSet<>(possibleSolutions), patternCountsByWord);

        Map<String, Double> expectedScoreByWord = Map.ofEntries(
            entry("raise", -0.5),
            entry("slate", -1.5),
            entry("parse", -0.5),
            entry("bubby", -3.0),
            entry("yippy", -1.0)
        );
        assertEquals(expectedScoreByWord, scoreByWord);
    }

}
