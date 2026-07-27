package com.damik3.solver.pattern.entropy;

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
     * Avg steps:  3.592  (min 2, max 6)
     * Histogram:  2:18  3:439  4:478  5:63  6:2
     * Elapsed:    290.03s  (3.4 games/s)
     */

    /**
     * After calculateGuess performance improvement
     *
     * === benchmark results ===
     * Games:      1000
     * Solved:     1000 (100.0%)
     * Failed:     0
     * Avg steps:  3.601  (min 2, max 6)
     * Histogram:  2:18  3:426  4:495  5:59  6:2
     * Elapsed:    144.98s  (6.9 games/s)
     */

   /**
    * After calculatePatternCounts stream to loop improvement
    * *
    * === benchmark results ===
    * Games:      1000
    * Solved:     1000 (100.0%)
    * Failed:     0
    * Avg steps:  3.602  (min 2, max 6)
    * Histogram:  2:18  3:426  4:494  5:60  6:2
    * Elapsed:    39.10s  (25.6 games/s)
    */

    @Test
    void runBenchmark() throws IOException {
        BenchmarkResults results = Benchmark.run(new EntropySolver());
        System.out.println(results);
    }
}
