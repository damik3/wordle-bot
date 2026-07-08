package com.damik3;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordleBotTest {

    @Test
    void parseWordsFile_loadsWordsFromResourceAndNormalizesThem() throws Exception {
        WordleBot bot = new WordleBot("test-parse-words-file.txt");
        List<String> words = bot.possibleSolutions;
        assertFalse(words.isEmpty(), "Expected words.txt to provide at least one word");
        assertEquals(3, words.size());
        assertEquals("rossa", words.get(0));
        assertEquals("jetty", words.get(1));
        assertEquals("wizzo", words.get(2));
        assertTrue(words
                .stream()
                .allMatch(word -> word.equals(word.toLowerCase())),
            "Every parsed word should be lowercase");
        assertTrue(words
            .stream()
            .noneMatch(String::isBlank), "Blank lines should be ignored during parsing");
    }

    @Test
    void eliminateWords_works() throws IOException {
        WordleBot bot = new WordleBot("test-eliminate-words.txt");
        List<Guess> guesses = new ArrayList<>(
            List.of(new Guess('s', 0, GuessResult.CorrectPosition),
                new Guess('t', 1, GuessResult.WrongPosition),
                new Guess('a', 2, GuessResult.CorrectPosition),
                new Guess('l', 3, GuessResult.NotExists),
                new Guess('e', 4, GuessResult.CorrectPosition)));
        bot.eliminateWords(guesses);
        List<String> words = bot.possibleSolutions;
        assertFalse(words.isEmpty(), "Expected words to have at least one word");
        assertEquals(2, words.size());
        assertLinesMatch(words, List.of("skate", "snate"));
    }

    @Test
    void calculateGuess_Result_works() throws IOException {
        WordleBot bot = new WordleBot("test-words.txt");
        String guess = "slate";
        String solution = "story";
        List<GuessResult> result = bot.calculateGuessResult(guess, solution);
        assertEquals(result,
            List.of(GuessResult.CorrectPosition, GuessResult.NotExists, GuessResult.NotExists,
                GuessResult.WrongPosition, GuessResult.NotExists));
    }

    @Test
    void calculatePatternCounts_works() throws IOException {
        WordleBot bot = new WordleBot("calculate-pattern-counts.txt");
        Map<String, Map<List<GuessResult>, Integer>> patternCountsByWord = bot.calculatePatternCounts();
        System.out.println(patternCountsByWord);
        // TODO: Add assertions
    }

    @Test
    void calculateNextBestGuess_works() throws IOException {
        WordleBot bot = new WordleBot("calculate-pattern-counts.txt");
        Map<String, Map<List<GuessResult>, Integer>> patternCountsByWord =
            bot.calculatePatternCounts();
        Map<String, Stats> statsByWord = bot.calculateStatsByWord(patternCountsByWord);
        System.out.println(statsByWord);
        String guess = bot.calculateNextBestGuess(statsByWord);
        System.out.println(guess);
    }

    @Test
    void isPreviousGuess_works() throws IOException {
        WordleBot bot = new WordleBot("test-words.txt");
        String word = "slate";
        List<Guess> previousGuess = List.of(
            new Guess('s', 0, GuessResult.WrongPosition),
            new Guess('l', 1, GuessResult.CorrectPosition),
            new Guess('a', 2, GuessResult.NotExists),
            new Guess('t', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.NotExists)
        );
        assertTrue(bot.isPreviousGuess(word, previousGuess));
    }

    @Test
    void containsNonExistingLetters_works() throws IOException {
        WordleBot bot = new WordleBot("test-words.txt");
        String word1 = "ffaff";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(bot.containsNonExistingLetters(word1, previousGuess1));

        String word2 = "fffff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(bot.containsNonExistingLetters(word2, previousGuess2));
    }

    @Test
    void containsExistingLetters_works() throws IOException {
        WordleBot bot = new WordleBot("test-words.txt");
        String word1 = "fffbc";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.NotExists)
        );
        assertTrue(bot.containsExistingLetters(word1, previousGuess1));

        String word2 = "fffff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.NotExists)
        );
        assertFalse(bot.containsExistingLetters(word2, previousGuess2));
    }

    @Test
    void correctLettersAreNotInCorrectPosition_works() throws IOException {
        WordleBot bot = new WordleBot("test-words.txt");
        String word1 = "bffff";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(bot.correctLettersAreNotInCorrectPosition(word1, previousGuess1));

        String word2 = "fbfff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(bot.correctLettersAreNotInCorrectPosition(word2, previousGuess2));
    }

    @Test
    void existingLettersAreInWrongPosition_works() throws IOException {
        WordleBot bot = new WordleBot("test-words.txt");
        String word1 = "ffffe";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.NotExists),
            new Guess('c', 2, GuessResult.NotExists),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(bot.existingLettersAreInWrongPosition(word1, previousGuess1));

        String word2 = "fffef";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.NotExists),
            new Guess('c', 2, GuessResult.NotExists),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(bot.existingLettersAreInWrongPosition(word2, previousGuess2));
    }

    @Test
    void play() throws IOException {
        WordleBot bot = new WordleBot("words.txt");
        String guess = null;
        List<Guess> guessResult = new ArrayList<>();

        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('s', 0, GuessResult.NotExists),
                new Guess('l', 1, GuessResult.NotExists),
                new Guess('a', 2, GuessResult.WrongPosition),
                new Guess('t', 3, GuessResult.WrongPosition),
                new Guess('e', 4, GuessResult.NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('t', 0, GuessResult.WrongPosition),
                new Guess('a', 1, GuessResult.CorrectPosition),
                new Guess('c', 2, GuessResult.WrongPosition),
                new Guess('i', 3, GuessResult.NotExists),
                new Guess('t', 4, GuessResult.WrongPosition)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);
        guessResult = new ArrayList<>(
            List.of(
                new Guess('w', 0, GuessResult.NotExists),
                new Guess('o', 1, GuessResult.NotExists),
                new Guess('m', 2, GuessResult.NotExists),
                new Guess('b', 3, GuessResult.WrongPosition),
                new Guess('s', 4, GuessResult.NotExists)
            )
        );
        guess = bot.nextGuess(guessResult);
        System.out.println("Guess: " + guess);

    }

}
