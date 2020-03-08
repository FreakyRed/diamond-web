package com.company.gamestudio.game.core;

import com.company.gamestudio.game.core.players.BlackPlayer;
import com.company.gamestudio.game.core.players.Player;
import com.company.gamestudio.game.core.players.WhitePlayer;

import java.util.*;

public class Triangle extends Tile {

    private List<Piece> pieces = new ArrayList<>();

    public Triangle() {

    }

    public Triangle(List<Piece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public List<Piece> getPieces() {
        return pieces;
    }

    @Override
    protected void addPiece(Piece piece) {
        this.pieces.add(piece);
    }

    public boolean isCapturable(Player player) {
        int countBlack = 0, countWhite = 0;
        for (Piece piece : pieces) {
            if (piece.getPieceType() == PieceType.BLACK) {
                countBlack++;
            }
            if (piece.getPieceType() == PieceType.WHITE) {
                countWhite++;
            }
        }

        if ((countBlack == 2 && countWhite == 1 && player instanceof BlackPlayer) ||
                (countBlack == 1 && countWhite == 2 && player instanceof WhitePlayer)) {
            return true;
        }

        return pieces.stream()
                .anyMatch(Piece::surroundedByEnemyPieces);
    }

    public Piece findCapturablePiece(Player player) {
        if (pieces.stream().anyMatch(Piece::surroundedByEnemyPieces)) {
            return pieces.stream()
                    .filter(p -> p.surroundedByEnemyPieces())
                    .findFirst()
                    .get();
        }

        return pieces.stream()
                .filter(p -> p.getPieceType() != player.getColour())
                .findFirst()
                .get();
    }

}
