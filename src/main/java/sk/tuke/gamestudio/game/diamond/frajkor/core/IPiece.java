package sk.tuke.gamestudio.game.diamond.frajkor.core;

public abstract class IPiece {
    private PieceType type = PieceType.EMPTY;

    public PieceType getType() {
        return type;
    }
}
