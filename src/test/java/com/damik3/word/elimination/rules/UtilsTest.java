package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.damik3.model.Guess.Result.CorrectPosition;
import static com.damik3.model.Guess.Result.NotExists;
import static com.damik3.model.Guess.Result.WrongPosition;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    @Test
    void getLimitsByCharacter_works_1() {
        List<Guess> guesses = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        Map<Character, Utils.Limit> limitsByCharacter = Utils.getLimitsByCharacter(guesses);
        assertEquals(new Utils.Limit(1, true), limitsByCharacter.get('a'));
        assertEquals(new Utils.Limit(0, true), limitsByCharacter.get('f'));
    }

    @Test
    void getLimitsByCharacter_works_2() {
        List<Guess> guesses = List.of(
            new Guess('a', 0, WrongPosition),
            new Guess('a', 1, NotExists),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        Map<Character, Utils.Limit> limitsByCharacter = Utils.getLimitsByCharacter(guesses);
        assertEquals(new Utils.Limit(1, true), limitsByCharacter.get('a'));
        assertEquals(new Utils.Limit(0, true), limitsByCharacter.get('f'));
    }

    @Test
    void getLimitsByCharacter_works_3() {
        List<Guess> guesses = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, CorrectPosition),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        Map<Character, Utils.Limit> limitsByCharacter = Utils.getLimitsByCharacter(guesses);
        assertEquals(new Utils.Limit(2, false), limitsByCharacter.get('a'));
        assertEquals(new Utils.Limit(0, true), limitsByCharacter.get('f'));
    }

    @Test
    void getLimitsByCharacter_works_4() {
        List<Guess> guesses = List.of(
            new Guess('a', 0, CorrectPosition),
            new Guess('a', 1, WrongPosition),
            new Guess('f', 2, NotExists),
            new Guess('f', 3, NotExists),
            new Guess('f', 4, NotExists)
        );
        Map<Character, Utils.Limit> limitsByCharacter = Utils.getLimitsByCharacter(guesses);
        assertEquals(new Utils.Limit(2, false), limitsByCharacter.get('a'));
        assertEquals(new Utils.Limit(0, true), limitsByCharacter.get('f'));
    }

    @Test
    void getCountByCharacter_works() {
        String word = "palla";
        Map<Character, Long> limitsByCharacter = Utils.getCountByCharacter(word);
        Map<Character, Long> expectedLimitsByCharacter = Map.of('p', 1L, 'a', 2L, 'l', 2L);
        assertEquals(expectedLimitsByCharacter, limitsByCharacter);
    }

}
