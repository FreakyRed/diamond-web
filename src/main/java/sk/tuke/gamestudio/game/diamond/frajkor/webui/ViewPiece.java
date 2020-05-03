package sk.tuke.gamestudio.game.diamond.frajkor.webui;

import sk.tuke.gamestudio.game.diamond.frajkor.core.Piece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.PieceType;

public class ViewPiece {
    private final Piece piece;
    private final int row;
    private final int column;

    public ViewPiece(Piece piece, int row, int column) {
        this.piece = piece;
        this.row = row;
        this.column = column;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isEmpty(){
        return piece.getPieceType() == PieceType.EMPTY;
    }

    public boolean isTaken(){
        return piece.getPieceType() != PieceType.EMPTY;
    }

    public String getType(){
        return piece.getPieceType().toString().toLowerCase();
    }

    public String getName(){
        return piece.getClass().getSimpleName().toLowerCase();
    }
}
