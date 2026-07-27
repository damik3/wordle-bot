package com.damik3.solver.pattern;

import com.damik3.Rules;
import com.damik3.model.Guess;
import com.damik3.model.Word;
import com.damik3.solver.Solver;

import java.util.*;

public abstract class PatternSolver implements Solver {

    public abstract Double getScore(Map<List<Guess.Result>, Integer> patternCounts, Boolean isPossibleSolution);

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
        Map<String, Double> statsByWord = calculateScoreByWord(new HashSet<>(possibleSolutions), patternCountsByWord);
        return calculateNextBestGuess(statsByWord);
    }

    static public String calculateNextBestGuess(Map<String, Double> statsByWord) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(statsByWord.entrySet());
        return entryList
            .stream()
            .max(Comparator.comparingDouble(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public Map<String, Double> calculateScoreByWord(
        HashSet<String> possibleSolutions,
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord
    ) {
        Map<String, Double> entropyByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Boolean isPossibleSolution = possibleSolutions.contains(s);
            Double score = getScore(patternCounts, isPossibleSolution);
            entropyByWord.put(s, score);
        });
        return entropyByWord;
    }

    static public Map<String, Map<List<Guess.Result>, Integer>> calculatePatternCounts(List<String> words,
                                                                         List<String> possibleSolutions) {
        Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord = new HashMap<>();
        words.forEach(nextGuess -> {
            Map<List<Guess.Result>, Integer> patternCounts = new HashMap<>();
            possibleSolutions.forEach(possibleSolution -> {
                List<Guess> guesses = Rules.calculateGuess(nextGuess, possibleSolution);
                List<Guess.Result> guessResults = new ArrayList<>(guesses.size());
                for (Guess g : guesses) {
                    guessResults.add(g.guessResult);
                }
                patternCounts.merge(guessResults, 1, Integer::sum);
            });
            patternCountsByWord.put(nextGuess, patternCounts);
        });
        return patternCountsByWord;
    }

}
