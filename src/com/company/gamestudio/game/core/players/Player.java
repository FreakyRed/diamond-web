package com.company.gamestudio.game.core.players;

import com.company.gamestudio.game.core.PieceType;

public abstract class Player {
    public abstract PieceType getColour();
    public abstract int getRemainingPieces();
}
