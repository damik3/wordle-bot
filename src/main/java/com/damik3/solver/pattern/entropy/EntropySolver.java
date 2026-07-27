package com.damik3.solver.pattern.entropy;

import com.damik3.model.Guess;
import com.damik3.solver.pattern.PatternSolver;

import java.util.*;

public class EntropySolver extends PatternSolver {
    public static final double POSSIBLE_SOLUTION_BIAS = 0.02;

    @Override
    public Double getScore(Map<List<Guess.Result>, Integer> patternCounts, Boolean isPossibleSolution) {
        int total = 0;
        double entropy = 0.0;

        for (int patternCount : patternCounts.values()) {
            total += patternCount;
        }

        for (int patternCount : patternCounts.values()) {
            double p = (double) patternCount / total;
            entropy += (-1) * p * Math.log(p) / Math.log(2.0);
        }

        entropy += isPossibleSolution ? POSSIBLE_SOLUTION_BIAS : 0;

        return entropy;
    }

}
