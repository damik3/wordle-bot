package com.damik3.solver.pattern.minmax;

import com.damik3.model.Guess;
import com.damik3.solver.pattern.PatternSolver;

import java.util.*;

public class MinmaxSolver extends PatternSolver {
    public static final double POSSIBLE_SOLUTION_BIAS = 0.5;

    @Override
    public Double getScore(Map<List<Guess.Result>, Integer> patternCounts, Boolean isPossibleSolution) {
        int largestGroup = 0;
        for (int count: patternCounts.values())
            if (count > largestGroup)
                largestGroup = count;
        return (-1) * largestGroup + (isPossibleSolution ? POSSIBLE_SOLUTION_BIAS : 0);
    }

}
