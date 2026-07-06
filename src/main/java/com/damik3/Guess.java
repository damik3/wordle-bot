package com.damik3;

public class Guess {
    Character letter;
    GuessResult guessResult;

    public Guess(Character letter, GuessResult guessResult) {
        this.letter = letter;
        this.guessResult = guessResult;
    }

    @Override
    public String toString() {
        return "LetterGuess{" + "letter=" + letter + ", guessResult=" + guessResult + '}';
    }
}
