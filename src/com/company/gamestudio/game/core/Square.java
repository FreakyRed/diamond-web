package com.company.gamestudio.game.core;

import java.util.ArrayList;
import java.util.Collections;
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

    public boolean filledWithOneColour(PieceType colour) {
        return pieces.stream()
                .allMatch(p -> p.getPieceType() == colour);
    }

}
