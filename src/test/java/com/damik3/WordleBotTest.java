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
        List<String> words = bot.words;
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
            List.of(new Guess('s', GuessResult.CorrectPosition),
                new Guess('t', GuessResult.WrongPosition),
                new Guess('a', GuessResult.CorrectPosition),
                new Guess('l', GuessResult.NotExists),
                new Guess('e', GuessResult.CorrectPosition)));
        bot.eliminateWords(guesses);
        List<String> words = bot.words;
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
    void nextBestGuess_works() throws IOException {
        WordleBot bot = new WordleBot("calculate-pattern-counts.txt");
        Map<String, Map<List<GuessResult>, Integer>> patternCountsByWord =
            bot.calculatePatternCounts();
        Map<String, Stats> statsByWord = bot.calculateStatsByWord(patternCountsByWord);
        System.out.println(statsByWord);
        String guess = bot.nextBestGuess(statsByWord);
        System.out.println(guess);
    }

}
