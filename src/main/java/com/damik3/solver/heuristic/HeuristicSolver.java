package com.damik3.solver.heuristic;

import com.damik3.Rules;
import com.damik3.model.Guess;
import com.damik3.model.Word;
import com.damik3.solver.Solver;
import com.damik3.solver.heuristic.model.Stats;

import java.util.*;
import java.util.stream.Collectors;

public class HeuristicSolver implements Solver {

    @Override
    public String firstGuess(List<Word> words) {
        return words
            .stream()
            .min(Comparator.comparingDouble(w -> w.expectedAdditionalGuesses))
            .map(w -> w.word)
            .orElse(null);
    }

    @Override
    public String nextGuess(List<String> words, List<String> possibleSolutions) {
        if (possibleSolutions.size() == 1 || possibleSolutions.size() == 2)
            return possibleSolutions.get(0);
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = calculatePatternCounts(words,
            possibleSolutions);
        Map<String, Stats> statsByWord = calculateStatsByWord(possibleSolutions, patternCountsByWord);
        return calculateNextBestGuess(statsByWord);
    }

    String calculateNextBestGuess(Map<String, Stats> statsByWord) {
        List<Map.Entry<String, Stats>> entryList = new ArrayList<>(statsByWord.entrySet());
        return entryList
            .stream()
            .max(Comparator.comparingDouble((Map.Entry<String, Stats> e) -> e.getValue().score))
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    Map<String, Stats> calculateStatsByWord(List<String> possibleSolutions, Map<String, Map<List<Guess.Result>,
        Integer>> patternCountsByWord) {
        Map<String, Stats> statsByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Integer numberOfGroups = patternCounts.size();
            Integer largestGroup = patternCounts
                .values()
                .stream()
                .max(Comparator.comparingInt(i -> i))
                .orElse(0);
            Double isPossibleSolution = possibleSolutions.contains(s) ? 1.0 : 0;
            statsByWord.put(s, new Stats(numberOfGroups, largestGroup, isPossibleSolution));
        });
        return statsByWord;
    }

    Map<String, Map<List<Guess.Result>, Integer>> calculatePatternCounts(List<String> words,
                                                                         List<String> possibleSolutions) {
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = new HashMap<>();
        words.forEach(nextGuess -> {
            Map<List<Guess.Result>, Integer> patternCounts = new HashMap<>();
            possibleSolutions.forEach(possibleSolution -> {
                List<Guess.Result> guessResults = Rules
                    .calculateGuess(nextGuess, possibleSolution)
                    .stream()
                    .map(g -> g.guessResult)
                    .collect(Collectors.toList());
                patternCounts.merge(guessResults, 1, Integer::sum);
            });
            patternCountsByWord.put(nextGuess, patternCounts);
        });
        return patternCountsByWord;
    }
}
