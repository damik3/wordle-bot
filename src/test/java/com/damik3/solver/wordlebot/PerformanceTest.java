package com.damik3.solver.wordlebot;

import com.damik3.Wordle;
import com.damik3.solver.PerformanceTestResults;
import com.damik3.solver.PerformanceTestResults.GameOutcome;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Tag("benchmark")
public class PerformanceTest {

    private static final int SAMPLE_SIZE = 100;
    private static final long SHUFFLE_SEED = 42L;

    @Test
    void runBenchmark() throws IOException {
        Wordle template = new Wordle();
        template.setSolver(new WordleBotSolver());
        template.loadWords("words.txt");

        List<String> sample = new ArrayList<>(template.getPossibleSolutions());
        Collections.shuffle(sample, new Random(SHUFFLE_SEED));
        if (sample.size() > SAMPLE_SIZE) {
            sample = sample.subList(0, SAMPLE_SIZE);
        }

        long startNs = System.nanoTime();
        List<GameOutcome> outcomes = sample
            .parallelStream()
            .map(solution -> {
                Wordle wordle = template.shallowCopy();
                return new GameOutcome(solution, wordle.play(solution));
            })
            .collect(Collectors.toList());
        long elapsedMs = (System.nanoTime() - startNs) / 1_000_000L;

        System.out.println(PerformanceTestResults.from(outcomes, elapsedMs));
    }
}

/**
 * === WordleBotSolver benchmark ===
 * Games:      1000
 * Solved:     1000 (100.0%)
 * Failed:     0
 * Avg steps:  3.591  (min 2, max 6)
 * Histogram:  2:19  3:440  4:473  5:67  6:1
 * Elapsed:    280.13s  (3.6 games/s)
 * */
