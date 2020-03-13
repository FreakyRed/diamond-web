package com.company.gamestudio.game.core.players;

import com.company.gamestudio.game.core.PieceType;

public class WhitePlayer extends Player {
    private String playerSymbol = "#";
    private PieceType colour = PieceType.WHITE;
    private int remainingPieces = 12;

    @Override
    public PieceType getColour() {
        return colour;
    }

    @Override
    public int getRemainingPieces() {
        return remainingPieces;
    }

    @Override
    public void setRemainingPieces(int remainingPieces) {
        this.remainingPieces = remainingPieces;
    }

    @Override
    public String toString() {
        return "WHITE";
    }
}
