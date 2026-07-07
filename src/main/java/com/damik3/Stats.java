package com.damik3;

public class Stats {
    static Double numberOfGroupsWeight = 0.33;
    static Double largestGroupWeight = 0.33;
    static Double isPossibleSolutionWeight = 0.33;

    Integer numberOfGroups;
    Integer largestGroup;
    Double isPossibleSolution;
    Double score;

    public Stats(Integer numberOfGroups, Integer largestGroup, Double isPossibleSolution) {
        this.numberOfGroups = numberOfGroups;
        this.largestGroup = largestGroup;
        this.isPossibleSolution = isPossibleSolution;
        this.score =
            numberOfGroupsWeight * numberOfGroups - largestGroupWeight * largestGroup + isPossibleSolutionWeight * isPossibleSolution;
    }

    @Override
    public String toString() {
        return "Stats{" + "numberOfGroups=" + numberOfGroups + ", largestGroup=" + largestGroup + ", score=" + score + '}';
    }
}
