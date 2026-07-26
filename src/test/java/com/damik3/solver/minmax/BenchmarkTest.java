package com.damik3.solver.minmax;

import com.damik3.solver.Benchmark;
import com.damik3.solver.BenchmarkResults;
import com.damik3.solver.pattern.minmax.MinmaxSolver;
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
     * Avg steps:  3.672  (min 2, max 6)
     * Histogram:  2:19  3:376  4:522  5:80  6:3
     * Elapsed:    34.25s  (29.2 games/s)
     */

    @Test
    void runBenchmark() throws IOException {
        BenchmarkResults results = Benchmark.run(new MinmaxSolver());
        System.out.println(results);
    }
}
