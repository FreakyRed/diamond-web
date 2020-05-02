package sk.tuke.gamestudio.game.diamond.frajkor.webui;

import sk.tuke.gamestudio.game.diamond.frajkor.core.IPiece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Piece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.PieceType;

public class ViewPiece<P extends IPiece> {
    private final P piece;
    private final int row;
    private final int column;

    public ViewPiece(P piece, int row, int column) {
        this.piece = piece;
        this.row = row;
        this.column = column;
    }

    public P getPiece() {
        return piece;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isEmpty(){
        return piece.getType() == PieceType.EMPTY;
    }

    public boolean isTaken(){
        return piece.getType() != PieceType.EMPTY;
    }

    public String getType(){
        return piece.getType().toString().toLowerCase();
    }

    public String getName(){
        return piece.getClass().getSimpleName().toLowerCase();
    }
}
