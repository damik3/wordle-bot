package com.damik3.solver.pattern.entropy;

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

public class EntropySolverTest extends PatternSolverTestBase {

    @Override
    protected Solver newSolver() {
        return new EntropySolver();
    }

    @Test
    void calculateScoreByWord_shouldWork() {
        EntropySolver entropySolver = new EntropySolver();

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
        Map<String, Double> scoreByWord = entropySolver.calculateScoreByWord(new HashSet<>(possibleSolutions), patternCountsByWord);

        Map<String, Double> expectedScoreByWord = Map.ofEntries(
            entry("raise", 1.58496250072 + EntropySolver.POSSIBLE_SOLUTION_BIAS),
            entry("slate", 0.91829583405 + EntropySolver.POSSIBLE_SOLUTION_BIAS),
            entry("parse", 1.58496250072 + EntropySolver.POSSIBLE_SOLUTION_BIAS),
            entry("bubby", 0.0),
            entry("yippy", 1.58496250072)
        );

        assertEquals(expectedScoreByWord.size(), scoreByWord.size());
        assertEquals(expectedScoreByWord.keySet(), scoreByWord.keySet());
        double EPS = 1e-9;
        expectedScoreByWord.forEach((word, expected) ->
            assertEquals(expected, scoreByWord.get(word), EPS, "word=" + word)
        );
    }

}
