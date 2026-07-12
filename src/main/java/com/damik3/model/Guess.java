package com.damik3.model;

public class Guess {

    public enum Result {
        NotExists, WrongPosition, CorrectPosition;

        @Override
        public String toString() {
            switch (this) {
                case NotExists:
                    return "_";
                case WrongPosition:
                    return "W";
                case CorrectPosition:
                    return "C";
                default:
                    return name(); // defensive fallback (should never happen)
            }
        }
    }

    public final Character letter;
    public final int index;
    public final Result guessResult;

    public Guess(Character letter, int index, Result guessResult) {
        this.letter = letter;
        this.index = index;
        this.guessResult = guessResult;
    }

    @Override
    public String toString() {
        return letter + "(" + guessResult + ")";
    }
}
