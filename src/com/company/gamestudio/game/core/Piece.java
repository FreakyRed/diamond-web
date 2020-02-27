package com.company.gamestudio.game.core;

import java.util.ArrayList;
import java.util.List;

public class Piece {

    private PieceType pieceType = PieceType.EMPTY;
    private List<Piece> connectedPieces = new ArrayList<>();

    public Piece() {

    }

    public Piece(List<Piece> connectedPieces) {
        this.connectedPieces = connectedPieces;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }


    public void setConnectedPieces(List<Piece> connectedPieces) {
        this.connectedPieces = connectedPieces;
    }

    public List<Piece> getConnectedPieces() {
        return connectedPieces;
    }

    public boolean isConnectedTo(Piece otherPiece) {
        return connectedPieces.contains(otherPiece);
    }
}

