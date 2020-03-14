package com.company.gamestudio.game.core;

import java.util.ArrayList;
import java.util.List;

public class Piece implements Comparable<Piece> {

    private PieceType pieceType = PieceType.EMPTY;
    private List<Piece> connectedPieces = new ArrayList<>();

    Piece() {

    }

    public Piece(List<Piece> connectedPieces) {
        this.connectedPieces = connectedPieces;
    }

    PieceType getPieceType() {
        return this.pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    void capture() {
        if (this.pieceType == PieceType.BLACK || this.pieceType == PieceType.WHITE) {
            this.setPieceType(PieceType.RED);
        }
    }

    boolean surroundedByEnemyPieces() {
        if (pieceType == PieceType.BLACK) {
            return this.getConnectedPieces().stream()
                    .map(Piece::getPieceType)
                    .allMatch(p -> p == PieceType.WHITE);
        }
        else if(pieceType == PieceType.WHITE) {
            return this.getConnectedPieces().stream()
                    .map(Piece::getPieceType)
                    .allMatch(p-> p == PieceType.BLACK);
        }

        return false;
    }

    boolean removeRedPiece() {
        if (this.getPieceType() == PieceType.RED) {
            this.setPieceType(PieceType.EMPTY);
            return true;
        }
        return false;
    }

    void addConnectedPiece(Piece connectedPiece) {
        this.connectedPieces.add(connectedPiece);
    }

    List<Piece> getConnectedPieces() {
        return connectedPieces;
    }

    boolean isConnectedTo(Piece otherPiece) {
        return connectedPieces.contains(otherPiece);
    }

    @Override
    public int compareTo(Piece o) {
        return this.hashCode() - o.hashCode();
    }
}

