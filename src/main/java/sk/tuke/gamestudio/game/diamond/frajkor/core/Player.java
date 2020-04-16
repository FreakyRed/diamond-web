package sk.tuke.gamestudio.game.diamond.frajkor.core;

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
