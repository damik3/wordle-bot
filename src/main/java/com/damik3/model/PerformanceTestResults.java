package com.damik3.model;

public class PerformanceTestResults {

    public final int total;
    public final int found;
    public final int notFound;

    public PerformanceTestResults(int total, int found, int notFound) {
        this.total = total;
        this.found = found;
        this.notFound = notFound;
    }

    @Override
    public String toString() {
        return "PerformanceTestResults{" + "total=" + total + ", found=" + found + ", notFound=" + notFound + '}';
    }
}
