package com.damik3.solver;

import com.damik3.model.Word;

import java.util.List;

public interface Solver {
    String firstGuess(List<Word> words);
    String nextGuess(List<String> words, List<String> possibleSolutions);
}
