package com.damik3;

public class Stats {
    static Double numberOfGroupsWeight = 0.5;
    static Double largestGroupWeight = 0.5;

    Integer numberOfGroups;
    Integer largestGroup;
    Double score;

    public Stats(Integer numberOfGroups, Integer largestGroup) {
        this.numberOfGroups = numberOfGroups;
        this.largestGroup = largestGroup;
        this.score = numberOfGroupsWeight * numberOfGroups - largestGroupWeight * largestGroup;
    }

    @Override
    public String toString() {
        return "Stats{" + "numberOfGroups=" + numberOfGroups + ", largestGroup=" + largestGroup + ", score=" + score + '}';
    }
}
