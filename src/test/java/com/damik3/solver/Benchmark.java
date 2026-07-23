package com.damik3.solver;

import com.damik3.Wordle;
import com.damik3.solver.BenchmarkResults.GameOutcome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Benchmark {

    private static final int SAMPLE_SIZE = 1000;
    private static final long SHUFFLE_SEED = 42L;

    public static BenchmarkResults run(Solver solver) throws IOException {
        Wordle template = new Wordle();
        template.setSolver(solver);
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

        return BenchmarkResults.from(outcomes, elapsedMs);
    }
}
