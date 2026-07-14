package com.damik3;

import com.damik3.model.Guess;
import com.damik3.model.Stats;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("raise",  nextBestGuess);
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
            entry(List.of(NotExists, NotExists, WrongPosition, WrongPosition, NotExists), 1)
        );
        Map<List<Guess.Result>, Integer> patternCountsForYippy = patternCounts.get("yippy");
        assertEquals(expectedPatternCountsForYippy, patternCountsForYippy);
    }


    @Test
    void calculateGuess_shouldWork() {
        String guessWord = "abcde";
        String solutionWord = "bcffe";
        List<Guess> guesses = WordleBot.calculateGuess(guessWord, solutionWord);
        assertEquals(5, guesses.size());

        Guess guess;
        guess = guesses.get(0);
        assertEquals(NotExists, guess.guessResult);
        assertEquals(0, guess.index);
        assertEquals('a', guess.letter);

        guess = guesses.get(1);
        assertEquals(WrongPosition, guess.guessResult);
        assertEquals(1, guess.index);
        assertEquals('b', guess.letter);

        guess = guesses.get(2);
        assertEquals(WrongPosition, guess.guessResult);
        assertEquals(2, guess.index);
        assertEquals('c', guess.letter);

        guess = guesses.get(3);
        assertEquals(NotExists, guess.guessResult);
        assertEquals(3, guess.index);
        assertEquals('d', guess.letter);

        guess = guesses.get(4);
        assertEquals(Guess.Result.CorrectPosition, guess.guessResult);
        assertEquals(4, guess.index);
        assertEquals('e', guess.letter);
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
        guessResult = new ArrayList<>(
            List.of(
                new Guess('t', 0, WrongPosition),
                new Guess('a', 1, Guess.Result.CorrectPosition),
                new Guess('c', 2, WrongPosition),
                new Guess('i', 3, NotExists),
                new Guess('t', 4, WrongPosition)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('w', 0, NotExists),
                new Guess('o', 1, NotExists),
                new Guess('m', 2, NotExists),
                new Guess('b', 3, WrongPosition),
                new Guess('s', 4, NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
    }

}
