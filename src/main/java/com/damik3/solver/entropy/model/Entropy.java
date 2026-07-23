package com.damik3.solver.entropy.model;

import com.damik3.model.Guess;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Entropy {
    public static final double POSSIBLE_SOLUTION_BIAS = 0.02;
    public final Double entropy;

    public Entropy(Double entropy) {
        this.entropy = entropy;
    }

    public Entropy(Map<List<Guess.Result>, Integer> patternCounts, Double isPossibleSolution) {
        int total = patternCounts
            .values()
            .stream()
            .mapToInt(i -> i)
            .sum();
        this.entropy = patternCounts
            .values()
            .stream()
            .mapToInt(i -> i)
            .mapToDouble(i -> {
                double p = (double) i / total;
                return (-1) * p * Math.log(p) / Math.log(2.0);
            })
            .sum() + POSSIBLE_SOLUTION_BIAS * isPossibleSolution;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Entropy entropy1 = (Entropy) o;
        return Objects.equals(entropy, entropy1.entropy);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entropy);
    }

    @Override
    public String toString() {
        return "Entropy{" + "entropy=" + entropy + '}';
    }
}
