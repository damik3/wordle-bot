package com.damik3.solver.entropy;

import com.damik3.Rules;
import com.damik3.model.Guess;
import com.damik3.model.Word;
import com.damik3.solver.Solver;
import com.damik3.solver.entropy.model.Entropy;

import java.util.*;
import java.util.stream.Collectors;

public class EntropySolver implements Solver {

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
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = calculatePatternCounts(words,
            possibleSolutions);
        Map<String, Entropy> entropyByWord = calculateEntropyByWord(new HashSet<>(possibleSolutions),
            patternCountsByWord);
        return calculateNextBestGuess(entropyByWord);
    }

    String calculateNextBestGuess(Map<String, Entropy> entropyByWord) {
        List<Map.Entry<String, Entropy>> entryList = new ArrayList<>(entropyByWord.entrySet());
        return entryList
            .stream()
            .max(Comparator.comparingDouble((Map.Entry<String, Entropy> e) -> e.getValue().entropy))
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    Map<String, Entropy> calculateEntropyByWord(Set<String> possibleSolutions, Map<String, Map<List<Guess.Result>,
        Integer>> patternCountsByWord) {
        Map<String, Entropy> entropyByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Double isPossibleSolution = possibleSolutions.contains(s) ? 1.0 : 0;
            entropyByWord.put(s, new Entropy(patternCounts, isPossibleSolution));
        });
        return entropyByWord;
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
