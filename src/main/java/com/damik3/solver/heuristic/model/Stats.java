package com.damik3.solver.heuristic.model;

import java.util.Objects;

public class Stats {
    final Integer numberOfGroups;
    final Integer largestGroup;
    final Double isPossibleSolution;
    public final Double score;

    public Stats(Integer numberOfGroups, Integer largestGroup, Double isPossibleSolution) {
        this.numberOfGroups = numberOfGroups;
        this.largestGroup = largestGroup;
        this.isPossibleSolution = isPossibleSolution;
        this.score = numberOfGroups - largestGroup + isPossibleSolution;
    }

    @Override
    public String toString() {
        return "Stats{" + "numberOfGroups=" + numberOfGroups + ", largestGroup=" + largestGroup + ", " +
            "isPossibleSolution=" + isPossibleSolution + ", score=" + score + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Stats stats = (Stats) o;
        return Objects.equals(numberOfGroups, stats.numberOfGroups) && Objects.equals(largestGroup,
            stats.largestGroup) && Objects.equals(isPossibleSolution, stats.isPossibleSolution) && Objects.equals(score,
            stats.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGroups, largestGroup, isPossibleSolution, score);
    }
}
