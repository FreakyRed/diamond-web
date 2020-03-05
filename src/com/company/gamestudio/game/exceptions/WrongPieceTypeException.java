package com.company.gamestudio.game.exceptions;

public class WrongPieceTypeException extends Exception {

    @Override
    public String toString() {
        return "WrongPieceTypeException";
    }

    @Override
    public String getMessage() {
        return "Wrong piece type";
    }
}
