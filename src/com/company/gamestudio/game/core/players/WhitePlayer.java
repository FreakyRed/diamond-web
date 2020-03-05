package com.company.gamestudio.game.core.players;

import com.company.gamestudio.game.core.PieceType;

public class WhitePlayer extends Player {
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
}
