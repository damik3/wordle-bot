package com.damik3.solver;

import java.util.List;

public interface Solver {
    String nextGuess(List<String> words, List<String> possibleSolutions);
}
