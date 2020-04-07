package gamestudio.game.diamond.frajkor.core;

import java.util.ArrayList;
import java.util.List;

public class Square extends Tile {

    private List<Piece> pieces = new ArrayList<>();

    Square() {

    }

    @Override
    public List<Piece> getPieces() {
        return this.pieces;
    }

    @Override
    protected void addPiece(Piece piece) {
        this.pieces.add(piece);
    }

    boolean isFilledWithOneColour(PieceType colour) {
        return pieces.stream()
                .allMatch(p -> p.getPieceType() == colour);
    }

}
