package com.damik3;

import com.damik3.model.Guess;
import com.damik3.model.Stats;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordleBotTest {

    @Test
    void calculateNextBestGuess_shouldWork() {
        Map<String, Stats> statsByWord = Map.ofEntries(
            entry("raise", new Stats(3, 1, 1.0)),
            entry("slate", new Stats(2, 2, 1.0)),
            entry("parse", new Stats(3, 1, 1.0)),
            entry("bubby", new Stats(1, 3, 0.0)),
            entry("yippy", new Stats(3, 1, 0.0))
        );

        String nextBestGuess = WordleBot.calculateNextBestGuess(statsByWord);
        assertTrue(Objects.equals(nextBestGuess, "raise") || Objects.equals(nextBestGuess, "parse"));
    }

    @Test
    void calculateStatsByWord_shouldWork() throws IOException {
        WordleBot wordleBot = new WordleBot("words_5.txt"); // raise, slate, parse, bubby, yippy

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
                entry(List.of(NotExists, NotExists, WrongPosition, WrongPosition, NotExists), 1)
            ))
        );

        Map<String, Stats> statsByWord = wordleBot.calculateStatsByWord(patternCountsByWord);

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
    void calculatePatternCounts_shouldWork() throws IOException {
        WordleBot wordleBot = new WordleBot("words_5.txt"); // raise, slate, parse, bubby, yippy
        Map<String, Map<List<Guess.Result>, Integer>> patternCounts = wordleBot.calculatePatternCounts();

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

    @Test
    void calculateGuess_shouldWork() {
        String guessWord = "abcde";
        String solutionWord = "bcffe";
        List<Guess> guesses = WordleBot.calculateGuess(guessWord, solutionWord);
        List<Guess> expectedGuesses = List.of(
            new Guess('a', 0, NotExists),
            new Guess('b', 1, WrongPosition),
            new Guess('c', 2, WrongPosition),
            new Guess('d', 3, NotExists),
            new Guess('e', 4, CorrectPosition)
        );
        assertEquals(expectedGuesses, guesses);
    }

    @Test
    void calculateGuess_shouldWorkForDoubleLetters_when1Exists_and2AreGuessed() {
        String guessWord1 = "aafff";
        String solutionWord1 = "abbbb";
        List<Guess> guesses1 = WordleBot.calculateGuess(guessWord1, solutionWord1);
        List<Guess> expectedGuesses1 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses1, guesses1);

        String guessWord2 = "fffaa";
        String solutionWord2 = "abbbb";
        List<Guess> guesses2 = WordleBot.calculateGuess(guessWord2, solutionWord2);
        List<Guess> expectedGuesses2 = List.of(
            new Guess('f', 0, NotExists),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('a', 3, WrongPosition),
            new Guess('a', 4, NotExists)
        );
        assertEquals(expectedGuesses2, guesses2);
    }

    @Test
    void calculateGuess_shouldWorkForDoubleLetters_when2Exist_and2AreGuessed() {
        String guessWord1 = "aafff";
        String solutionWord1 = "aabbb";
        List<Guess> guesses1 = WordleBot.calculateGuess(guessWord1, solutionWord1);
        List<Guess> expectedGuesses1 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, CorrectPosition),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses1, guesses1);

        String guessWord2 = "afaff";
        String solutionWord2 = "aabbb";
        List<Guess> guesses2 = WordleBot.calculateGuess(guessWord2, solutionWord2);
        List<Guess> expectedGuesses2 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('f', 1, NotExists),
            new Guess('a', 2, WrongPosition),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses2, guesses2);

        String guessWord3 = "fffaa";
        String solutionWord3 = "aabbb";
        List<Guess> guesses3 = WordleBot.calculateGuess(guessWord3, solutionWord3);
        List<Guess> expectedGuesses3 = List.of(
            new Guess('f', 0, NotExists),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('a', 3, WrongPosition),
            new Guess('a', 4, WrongPosition)
        );
        assertEquals(expectedGuesses3, guesses3);

    }

    @Test
    void calculateGuess_shouldWorkForDoubleLetters_when2Exist_and1IsGuessed() {
        String guessWord1 = "affff";
        String solutionWord1 = "aacde";
        List<Guess> guesses1 = WordleBot.calculateGuess(guessWord1, solutionWord1);
        List<Guess> expectedGuesses1 = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        assertEquals(expectedGuesses1, guesses1);

        String guessWord2 = "ffffa";
        String solutionWord2 = "aacde";
        List<Guess> guesses2 = WordleBot.calculateGuess(guessWord2, solutionWord2);
        List<Guess> expectedGuesses2 = List.of(
            new Guess('f', 0, NotExists),
            new Guess('f', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('a', 4, WrongPosition)
        );
        assertEquals(expectedGuesses2, guesses2);
    }

    @Test
    void WordleBot_shouldParseWordsFile() throws IOException {
        WordleBot wordleBot = new WordleBot("words_10.txt");
        List<String> words = wordleBot.words;
        List<String> possibleSolutions = wordleBot.possibleSolutions;
        String openingWord = wordleBot.openingWord;
        assertEquals(10, words.size());
        assertEquals(7, possibleSolutions.size());
        assertEquals("slate", openingWord);
    }

    @Test
    void play() throws IOException {
        WordleBot bot = new WordleBot("words.txt");
        String guess;
        List<Guess> guessResult = new ArrayList<>();

        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('s', 0, NotExists),
                new Guess('l', 1, NotExists),
                new Guess('a', 2, WrongPosition),
                new Guess('t', 3, WrongPosition),
                new Guess('e', 4, NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
    }

}
