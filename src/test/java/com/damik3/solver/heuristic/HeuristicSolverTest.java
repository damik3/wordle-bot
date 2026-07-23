package com.damik3.solver.heuristic;

import com.damik3.model.Guess;
import com.damik3.solver.heuristic.model.Stats;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeuristicSolverTest {

    @Test
    void calculateNextBestGuess_shouldWork() {
        HeuristicSolver heuristicSolver = new HeuristicSolver();
        Map<String, Stats> statsByWord = Map.ofEntries(
            entry("raise", new Stats(3, 1, 1.0)),
            entry("slate", new Stats(2, 2, 1.0)),
            entry("parse", new Stats(3, 1, 1.0)),
            entry("bubby", new Stats(1, 3, 0.0)),
            entry("yippy", new Stats(3, 1, 0.0))
        );

        String nextBestGuess = heuristicSolver.calculateNextBestGuess(statsByWord);
        assertTrue(Objects.equals(nextBestGuess, "raise") || Objects.equals(nextBestGuess, "parse"));
    }

    @Test
    void calculateStatsByWord_shouldWork() {
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

        List<String> possibleSolutions = List.of("raise", "slate", "parse");
        Map<String, Stats> statsByWord = heuristicSolver.calculateStatsByWord(possibleSolutions, patternCountsByWord);

        Map<String, Stats> expectedStatsByWord = Map.ofEntries(
            entry("raise", new Stats(3, 1, 1.0)),
            entry("slate", new Stats(2, 2, 1.0)),
            entry("parse", new Stats(3, 1, 1.0)),
            entry("bubby", new Stats(1, 3, 0.0)),
            entry("yippy", new Stats(3, 1, 0.0))
        );

        assertEquals(expectedStatsByWord, statsByWord);
    }

    @Test
    void calculatePatternCounts_shouldWork() {
        HeuristicSolver heuristicSolver = new HeuristicSolver();
        List<String> words = List.of("raise", "slate", "parse", "bubby", "yippy");
        List<String> possibleSolutions = List.of("raise", "slate", "parse");
        Map<String, Map<List<Guess.Result>, Integer>> patternCounts = heuristicSolver.calculatePatternCounts(words, possibleSolutions);

        Map<List<Guess.Result>, Integer> expectedPatternCountsForRaise = Map.ofEntries(
            entry(List.of(CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition), 1),
            entry(List.of(NotExists, WrongPosition, NotExists, WrongPosition, CorrectPosition), 1),
            entry(List.of(WrongPosition, CorrectPosition, NotExists, CorrectPosition, CorrectPosition), 1)
        );
        Map<List<Guess.Result>, Integer> patternCountsForRaise = patternCounts.get("raise");
        assertEquals(expectedPatternCountsForRaise, patternCountsForRaise);

        Map<List<Guess.Result>, Integer> expectedPatternCountsForSlate = Map.ofEntries(
            entry(List.of(WrongPosition, NotExists, WrongPosition, NotExists, CorrectPosition), 2),
            entry(List.of(CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition), 1)
        );
        Map<List<Guess.Result>, Integer> patternCountsForSlate = patternCounts.get("slate");
        assertEquals(expectedPatternCountsForSlate, patternCountsForSlate);

        Map<List<Guess.Result>, Integer> expectedPatternCountsForParse = Map.ofEntries(
            entry(List.of(NotExists, CorrectPosition, WrongPosition, CorrectPosition, CorrectPosition), 1),
            entry(List.of(NotExists, WrongPosition, NotExists, WrongPosition, CorrectPosition), 1),
            entry(List.of(CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition, CorrectPosition), 1)
        );
        Map<List<Guess.Result>, Integer> patternCountsForParse = patternCounts.get("parse");
        assertEquals(expectedPatternCountsForParse, patternCountsForParse);

        Map<List<Guess.Result>, Integer> expectedPatternCountsForBubby = Map.ofEntries(
            entry(List.of(NotExists, NotExists, NotExists, NotExists, NotExists), 3)
        );
        Map<List<Guess.Result>, Integer> patternCountsForBubby = patternCounts.get("bubby");
        assertEquals(expectedPatternCountsForBubby, patternCountsForBubby);

        Map<List<Guess.Result>, Integer> expectedPatternCountsForYippy = Map.ofEntries(
            entry(List.of(NotExists, WrongPosition, NotExists, NotExists, NotExists), 1),
            entry(List.of(NotExists, NotExists, NotExists, NotExists, NotExists), 1),
            entry(List.of(NotExists, NotExists, WrongPosition, NotExists, NotExists), 1)
        );
        Map<List<Guess.Result>, Integer> patternCountsForYippy = patternCounts.get("yippy");
        assertEquals(expectedPatternCountsForYippy, patternCountsForYippy);
    }


}
