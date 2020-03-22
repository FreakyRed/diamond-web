package main.java.gamestudio.game.core;

public enum Player {
    BLACK, WHITE;

    public PieceType getColour() {
        if (this == BLACK) {
            return PieceType.BLACK;
        } else {
            return PieceType.WHITE;
        }
    }
}
