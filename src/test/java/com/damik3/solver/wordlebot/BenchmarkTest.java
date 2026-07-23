package com.damik3.solver.wordlebot;

import com.damik3.solver.Benchmark;
import com.damik3.solver.BenchmarkResults;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;


@Tag("benchmark")
public class BenchmarkTest {

    @Test
    void runBenchmark() throws IOException {
        BenchmarkResults results = Benchmark.run(new WordleBotSolver());
        System.out.println(results);
    }
}
