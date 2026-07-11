package com.damik3;

public class Stats {
    Integer numberOfGroups;
    Integer largestGroup;
    Double isPossibleSolution;
    Double score;

    public Stats(Integer numberOfGroups, Integer largestGroup, Double isPossibleSolution) {
        this.numberOfGroups = numberOfGroups;
        this.largestGroup = largestGroup;
        this.isPossibleSolution = isPossibleSolution;
        this.score = numberOfGroups -  largestGroup +  isPossibleSolution;
    }

    @Override
    public String toString() {
        return "Stats{" + "numberOfGroups=" + numberOfGroups + ", largestGroup=" + largestGroup + ", isPossibleSolution=" + isPossibleSolution + ", score=" + score + '}';
    }
}
