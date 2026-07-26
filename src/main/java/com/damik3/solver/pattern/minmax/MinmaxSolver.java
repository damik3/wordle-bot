package com.damik3.solver.pattern.minmax;

import com.damik3.model.Guess;
import com.damik3.solver.pattern.PatternSolver;

import java.util.*;

public class MinmaxSolver extends PatternSolver {

    @Override
    public Map<String, Double> calculateScoreByWord(
        HashSet<String> possibleSolutions, Map<String,
        Map<List<Guess.Result>, Integer>> patternCountsByWord
    ) {
        Map<String, Double> scoreByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            int largestGroup = patternCounts
                .values()
                .stream()
                .max(Comparator.comparingInt(i -> i))
                .orElse(0);
            double isPossibleSolution = possibleSolutions.contains(s) ? 0.5 : 0;
            Double score = (-1.0) * largestGroup + isPossibleSolution;
            scoreByWord.put(s, score);
        });
        return scoreByWord;
    }

}
