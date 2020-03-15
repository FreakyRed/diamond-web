package com.company.gamestudio.game.core;

public enum Player {
    BLACK, WHITE;

    public PieceType getColour() {
        if (this == BLACK) {
            return PieceType.BLACK;
        } else {
            return PieceType.WHITE;
        }
    }

    public String getSymbol() {
        if (this == BLACK) {
            return "&";
        } else {
            return "$";
        }
    }
}
