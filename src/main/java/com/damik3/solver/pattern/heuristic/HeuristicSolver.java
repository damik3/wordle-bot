package com.damik3.solver.pattern.heuristic;

import com.damik3.model.Guess;
import com.damik3.solver.pattern.PatternSolver;
import com.damik3.solver.pattern.heuristic.model.Stats;

import java.util.*;

public class HeuristicSolver extends PatternSolver {

    @Override
    public Map<String, Double> calculateScoreByWord(
        HashSet<String> possibleSolutions, Map<String,
        Map<List<Guess.Result>, Integer>> patternCountsByWord
    ) {
        Map<String, Double> scoreByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Integer numberOfGroups = patternCounts.size();
            Integer largestGroup = patternCounts
                .values()
                .stream()
                .max(Comparator.comparingInt(i -> i))
                .orElse(0);
            Double isPossibleSolution = possibleSolutions.contains(s) ? 1.0 : 0;
            Stats stats = new Stats(numberOfGroups, largestGroup, isPossibleSolution);
            scoreByWord.put(s, stats.score);
        });
        return scoreByWord;
    }

}
