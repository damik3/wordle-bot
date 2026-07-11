package com.damik3;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordleBotTest {

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
    void isPreviousGuess_works() {
        String word = "slate";
        List<Guess> previousGuess = List.of(
            new Guess('s', 0, GuessResult.WrongPosition),
            new Guess('l', 1, GuessResult.CorrectPosition),
            new Guess('a', 2, GuessResult.NotExists),
            new Guess('t', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.NotExists)
        );
        assertTrue(WordleBot.isPreviousGuess(word, previousGuess));
    }

    @Test
    void containsNonExistingLetters_works() {
        String word1 = "ffaff";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(WordleBot.containsNonExistingLetters(word1, previousGuess1));

        String word2 = "fffff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(WordleBot.containsNonExistingLetters(word2, previousGuess2));

        String word3 = "abcde";
        List<Guess> previousGuess3 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(WordleBot.containsNonExistingLetters(word3, previousGuess3));
    }

    @Test
    void containsExistingLetters_works() {
        String word1 = "fffbc";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.NotExists)
        );
        assertTrue(WordleBot.containsExistingLetters(word1, previousGuess1));

        String word2 = "fffff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.NotExists)
        );
        assertFalse(WordleBot.containsExistingLetters(word2, previousGuess2));
    }

    @Test
    void correctLettersAreNotInCorrectPosition_works() {
        String word1 = "bffff";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(WordleBot.correctLettersAreInCorrectPosition(word1, previousGuess1));

        String word2 = "fbfff";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.CorrectPosition),
            new Guess('c', 2, GuessResult.WrongPosition),
            new Guess('d', 3, GuessResult.WrongPosition),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(WordleBot.correctLettersAreInCorrectPosition(word2, previousGuess2));
    }

    @Test
    void existingLettersAreInADifferentPosition_works() {
        String word1 = "ffffe";
        List<Guess> previousGuess1 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.NotExists),
            new Guess('c', 2, GuessResult.NotExists),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(WordleBot.existingLettersAreInADifferentPosition(word1, previousGuess1));

        String word2 = "fffef";
        List<Guess> previousGuess2 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.NotExists),
            new Guess('c', 2, GuessResult.NotExists),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertTrue(WordleBot.existingLettersAreInADifferentPosition(word2, previousGuess2));

        String word3 = "fffff";
        List<Guess> previousGuess3 = List.of(
            new Guess('a', 0, GuessResult.NotExists),
            new Guess('b', 1, GuessResult.NotExists),
            new Guess('c', 2, GuessResult.NotExists),
            new Guess('d', 3, GuessResult.NotExists),
            new Guess('e', 4, GuessResult.WrongPosition)
        );
        assertFalse(WordleBot.existingLettersAreInADifferentPosition(word3, previousGuess3));
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
