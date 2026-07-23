package com.damik3.solver.heuristic;

import com.damik3.solver.Benchmark;
import com.damik3.solver.BenchmarkResults;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;


@Tag("benchmark")
public class BenchmarkTest {

    /**
     * === benchmark results ===
     * Games:      1000
     * Solved:     1000 (100.0%)
     * Failed:     0
     * Avg steps:  3.591  (min 2, max 6)
     * Histogram:  2:19  3:440  4:473  5:67  6:1
     * Elapsed:    284.92s  (3.5 games/s)
     */

    @Test
    void runBenchmark() throws IOException {
        BenchmarkResults results = Benchmark.run(new HeuristicSolver());
        System.out.println(results);
    }
}
