package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {

    static class Limit {
        int limit;
        boolean hard;

        static Limit Default() {
            return new Limit(0, false);
        }

        public Limit(int limit, boolean hard) {
            this.limit = limit;
            this.hard = hard;
        }

        public Limit(List<Guess> guesses) {
            long existsCount = guesses
                .stream()
                .filter(g -> g.guessResult != Guess.Result.NotExists)
                .count();
            long notExistsCount = guesses
                .stream()
                .filter(g -> g.guessResult == Guess.Result.NotExists)
                .count();
            this.limit = (int) existsCount;
            this.hard = existsCount == 0 || notExistsCount > 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Limit limit1 = (Limit) o;
            return limit == limit1.limit && hard == limit1.hard;
        }

        @Override
        public int hashCode() {
            return Objects.hash(limit, hard);
        }

        @Override
        public String toString() {
            return "Limit{" + "limit=" + limit + ", hard=" + hard + '}';
        }
    }

    static Map<Character, Limit> getLimitsByCharacter(List<Guess> guesses) {
        Map<Character, List<Guess>> guessesByCharacter = guesses
            .stream()
            .collect(Collectors.groupingBy(g -> g.letter, Collectors.toList()));
        return guessesByCharacter
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> new Limit(entry.getValue())));
    }

    static Map<Character, Long> getCountByCharacter(String word) {
        return word.chars()
            .mapToObj(c -> (char) c) // Convert primitive int to Character object
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
    }

}
