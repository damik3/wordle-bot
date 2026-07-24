package com.damik3.solver.entropy;

import com.damik3.Wordle;
import com.damik3.model.Guess;
import com.damik3.solver.entropy.model.Entropy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntropySolverTest {

    @Test
    void play_shouldSolveSlate() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new EntropySolver());
        Wordle.Result result = wordle.play("slate");
        assertTrue(result.solved);
    }

    @Test
    void play_shouldNotThrowWhenThereAreNoPossibleSolutions() throws IOException {
        Wordle wordle = new Wordle();
        wordle.loadWords("words.txt");
        wordle.setSolver(new EntropySolver());
        Wordle.Result result = wordle.play("zzzzz");
        assertFalse(result.solved);
    }

    @Test
    void calculateNextBestGuess_shouldWork() {
        EntropySolver entropySolver = new EntropySolver();
        Map<String, Entropy> entropyByWord = Map.ofEntries(
            entry("raise", new Entropy(1.58496250072 + Entropy.POSSIBLE_SOLUTION_BIAS)),
            entry("slate", new Entropy(0.91829583405 + Entropy.POSSIBLE_SOLUTION_BIAS)),
            entry("parse", new Entropy(1.58496250072 + Entropy.POSSIBLE_SOLUTION_BIAS)),
            entry("bubby", new Entropy(0d)),
            entry("yippy", new Entropy(1.58496250072))
        );

        String nextBestGuess = entropySolver.calculateNextBestGuess(entropyByWord);
        assertTrue(Objects.equals(nextBestGuess, "raise") || Objects.equals(nextBestGuess, "parse"));
    }

    @Test
    void calculateStatsByWord_shouldWork() {
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
        Map<String, Entropy> entropyByWord = entropySolver.calculateEntropyByWord(new HashSet<>(possibleSolutions), patternCountsByWord);

        Map<String, Entropy> expectedEntropyByWord = Map.ofEntries(
            entry("raise", new Entropy(1.58496250072 + Entropy.POSSIBLE_SOLUTION_BIAS)),
            entry("slate", new Entropy(0.91829583405 + Entropy.POSSIBLE_SOLUTION_BIAS)),
            entry("parse", new Entropy(1.58496250072 + Entropy.POSSIBLE_SOLUTION_BIAS)),
            entry("bubby", new Entropy(0d)),
            entry("yippy", new Entropy(1.58496250072))
        );

        assertEquals(expectedEntropyByWord.size(), entropyByWord.size());
        assertEquals(expectedEntropyByWord.keySet(), entropyByWord.keySet());
        double EPS = 1e-9;
        expectedEntropyByWord.forEach((word, expected) ->
            assertEquals(expected.entropy, entropyByWord.get(word).entropy, EPS, "word=" + word)
        );
    }

    @Test
    void calculatePatternCounts_shouldWork() {
        EntropySolver entropySolver = new EntropySolver();
        List<String> words = List.of("raise", "slate", "parse", "bubby", "yippy");
        List<String> possibleSolutions = List.of("raise", "slate", "parse");
        Map<String, Map<List<Guess.Result>, Integer>> patternCounts = entropySolver.calculatePatternCounts(words, possibleSolutions);

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
