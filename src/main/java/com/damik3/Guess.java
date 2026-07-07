package com.damik3;

public class Guess {
    Character letter;
    int index;
    GuessResult guessResult;

    public Guess(Character letter, int index, GuessResult guessResult) {
        this.letter = letter;
        this.index = index;
        this.guessResult = guessResult;
    }

    @Override
    public String toString() {
        return "Guess{" + "letter=" + letter + ", index=" + index + ", guessResult=" + guessResult + '}';
    }
}
