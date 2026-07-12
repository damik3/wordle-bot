package com.damik3.word.elimination;

import com.damik3.model.Guess;

import java.util.List;

public interface WordEliminationRule {
    boolean apply(String word, List<Guess> previousGuess);
}

