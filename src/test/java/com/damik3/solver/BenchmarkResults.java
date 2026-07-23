package com.damik3.solver;

import com.damik3.Wordle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class BenchmarkResults {

    final int total;
    final int solved;
    final int failed;
    final double solveRate;
    final double averageSteps;
    final int minSteps;
    final int maxSteps;
    final Map<Integer, Integer> stepsHistogram;
    final List<String> failedSolutions;
    final long elapsedMs;

    public BenchmarkResults(
        int total,
        int solved,
        int failed,
        double solveRate,
        double averageSteps,
        int minSteps,
        int maxSteps,
        Map<Integer, Integer> stepsHistogram,
        List<String> failedSolutions,
        long elapsedMs
    ) {
        this.total = total;
        this.solved = solved;
        this.failed = failed;
        this.solveRate = solveRate;
        this.averageSteps = averageSteps;
        this.minSteps = minSteps;
        this.maxSteps = maxSteps;
        this.stepsHistogram = stepsHistogram;
        this.failedSolutions = failedSolutions;
        this.elapsedMs = elapsedMs;
    }

    public static BenchmarkResults from(List<GameOutcome> outcomes, long elapsedMs) {
        int total = outcomes.size();
        int solved = 0;
        long stepsSum = 0;
        int minSteps = Integer.MAX_VALUE;
        int maxSteps = 0;
        Map<Integer, Integer> histogram = new TreeMap<>();
        List<String> failedSolutions = new ArrayList<>();

        for (GameOutcome outcome : outcomes) {
            Wordle.Result result = outcome.result;
            if (result.solved) {
                solved++;
                stepsSum += result.steps;
                minSteps = Math.min(minSteps, result.steps);
                maxSteps = Math.max(maxSteps, result.steps);
                histogram.merge(result.steps, 1, Integer::sum);
            } else {
                failedSolutions.add(outcome.solution + " → " + result.guesses);
            }
        }

        int failed = total - solved;
        double solveRate = total == 0 ? 0 : (100.0 * solved) / total;
        double averageSteps = solved == 0 ? 0 : (double) stepsSum / solved;
        if (solved == 0) {
            minSteps = 0;
        }

        return new BenchmarkResults(
            total,
            solved,
            failed,
            solveRate,
            averageSteps,
            minSteps,
            maxSteps,
            histogram,
            failedSolutions,
            elapsedMs
        );
    }

    @Override
    public String toString() {
        String hist = stepsHistogram
            .entrySet()
            .stream()
            .map(e -> e.getKey() + ":" + e.getValue())
            .collect(Collectors.joining("  "));
        if (hist.isEmpty()) {
            hist = "(none)";
        }

        double gamesPerSec = elapsedMs == 0 ? 0 : total * 1000.0 / elapsedMs;
        StringBuilder sb = new StringBuilder();
        sb.append("=== WordleBotSolver benchmark ===\n");
        sb.append(String.format(Locale.ROOT, "Games:      %d%n", total));
        sb.append(String.format(Locale.ROOT, "Solved:     %d (%.1f%%)%n", solved, solveRate));
        sb.append(String.format(Locale.ROOT, "Failed:     %d%n", failed));
        sb.append(String.format(Locale.ROOT, "Avg steps:  %.3f  (min %d, max %d)%n", averageSteps, minSteps, maxSteps));
        sb.append(String.format(Locale.ROOT, "Histogram:  %s%n", hist));
        sb.append(String.format(Locale.ROOT, "Elapsed:    %.2fs  (%.1f games/s)%n", elapsedMs / 1000.0, gamesPerSec));
        if (!failedSolutions.isEmpty()) {
            sb.append("Failures:\n");
            for (String failure : failedSolutions) {
                sb.append("  - ").append(failure).append('\n');
            }
        }
        return sb.toString();
    }

    public static final class GameOutcome {
        final String solution;
        final Wordle.Result result;

        public GameOutcome(String solution, Wordle.Result result) {
            this.solution = solution;
            this.result = result;
        }
    }
}
