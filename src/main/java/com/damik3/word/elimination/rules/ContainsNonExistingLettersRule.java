package com.damik3.word.elimination.rules;

import com.damik3.model.Guess;
import com.damik3.word.elimination.WordEliminationRule;

import java.util.List;
import java.util.Map;

public class ContainsNonExistingLettersRule implements WordEliminationRule {
    @Override
    public boolean apply(String word, List<Guess> previousGuess) {
        Map<Character, Long> countsByChar = Utils.getCountByCharacter(word);
        Map<Character, Utils.Limit> limitByChar = Utils.getLimitsByCharacter(previousGuess);
        return countsByChar.entrySet().stream().anyMatch(entry -> {
            Character c = entry.getKey();
            Long count = entry.getValue();
            Utils.Limit limit = limitByChar.getOrDefault(c, Utils.Limit.Default());
            return count > limit.limit && limit.hard;
        });
    }
}
