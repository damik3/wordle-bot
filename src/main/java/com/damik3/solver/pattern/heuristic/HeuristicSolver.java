package com.damik3.solver.pattern.heuristic;

import com.damik3.model.Guess;
import com.damik3.solver.pattern.PatternSolver;

import java.util.*;

public class HeuristicSolver extends PatternSolver {
    public static final double POSSIBLE_SOLUTION_BIAS = 1.0;

    @Override
    public Double getScore(Map<List<Guess.Result>, Integer> patternCounts, Boolean isPossibleSolution) {
        int numberOfGroups = patternCounts.size();
        int largestGroup = 0;
        for (int count: patternCounts.values())
            if (count > largestGroup)
                largestGroup = count;
        return numberOfGroups - largestGroup + (isPossibleSolution ? POSSIBLE_SOLUTION_BIAS : 0);
    }

}
