package sk.tuke.gamestudio.game.diamond.frajkor.core;

import java.util.List;

public abstract class Tile {
    public abstract List<Piece> getPieces();
    protected abstract void addPiece(Piece piece);
}
