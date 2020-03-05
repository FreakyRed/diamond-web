package com.company.gamestudio.game.core.players;

import com.company.gamestudio.game.core.PieceType;

public class BlackPlayer extends Player {
    private PieceType colour = PieceType.BLACK;
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
