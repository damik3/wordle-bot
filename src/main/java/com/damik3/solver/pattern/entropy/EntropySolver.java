package com.damik3.solver.pattern.entropy;

import com.damik3.model.Guess;
import com.damik3.solver.pattern.PatternSolver;
import com.damik3.solver.pattern.entropy.model.Entropy;

import java.util.*;

public class EntropySolver extends PatternSolver {

    @Override
    public Map<String, Double> calculateScoreByWord(
            HashSet<String> possibleSolutions,
            Map<String, Map<List<Guess.Result>, Integer>> patternCountsByWord
    ) {
        Map<String, Double> entropyByWord = new HashMap<>();
        patternCountsByWord.forEach((s, patternCounts) -> {
            Double isPossibleSolution = possibleSolutions.contains(s) ? 1.0 : 0;
            Entropy entropy = new Entropy(patternCounts, isPossibleSolution);
            entropyByWord.put(s, entropy.entropy);
        });
        return entropyByWord;
    }

}
