package com.damik3;

public enum GuessResult {
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
                // defensive fallback (should never happen)
                return name();
        }
    }
}
