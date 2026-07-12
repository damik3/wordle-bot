package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;

import java.util.List;

public class Not implements WordEliminationRule {

    final WordEliminationRule rule;

    public Not(WordEliminationRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean apply(String word, List<Guess> previousGuess) {
        return !this.rule.apply(word, previousGuess);
    }
}
