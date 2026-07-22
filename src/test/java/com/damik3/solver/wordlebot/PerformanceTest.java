package com.damik3.solver.wordlebot;

import com.damik3.Wordle;
import com.damik3.model.PerformanceTestResults;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceTest {

    @Test
    public void test() throws IOException {
        Wordle outerWordle = new Wordle();
        outerWordle.loadWords("words.txt");

        AtomicInteger total = new AtomicInteger();
        AtomicInteger found = new AtomicInteger();
        AtomicInteger notFound = new AtomicInteger();

        int limit = 1000;
        List<String> possibleSolutions = outerWordle.getPossibleSolutions().subList(0, limit);
        possibleSolutions.parallelStream().forEach(possibleSolution -> {
            Wordle wordle = new Wordle();
            try {
                wordle.loadWords("words.txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            wordle.setSolver(new WordleBotSolver());
            Wordle.Result result = wordle.play(possibleSolution);
            total.getAndIncrement();
            found.addAndGet(result.solved ? 1 : 0);
            notFound.addAndGet(result.solved ? 0 : 1);
        });

        PerformanceTestResults results = new PerformanceTestResults(total.get(), found.get(), notFound.get());
        System.out.println(results);
    }


}
