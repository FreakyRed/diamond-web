package com.company.gamestudio.game.core;

import com.company.gamestudio.game.exceptions.WrongPieceTypeException;

import java.util.ArrayList;
import java.util.List;

public class Piece implements Comparable<Piece> {

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

    public void capture() {
        if (this.pieceType == PieceType.BLACK || this.pieceType == PieceType.WHITE) {
            this.setPieceType(PieceType.RED);
        }
    }

    public boolean surroundedByEnemyPieces() {
        if (pieceType == PieceType.BLACK) {
            return this.getConnectedPieces().stream()
                    .map(p -> p.getPieceType())
                    .allMatch(p -> p == PieceType.WHITE);
        }
        else if(pieceType == PieceType.WHITE) {
            return this.getConnectedPieces().stream()
                    .map(p -> p.getPieceType())
                    .allMatch(p-> p == PieceType.BLACK);
        }

        return false;
    }

    public void removeRedPiece() {
        if (this.getPieceType() == PieceType.RED) {
            this.setPieceType(PieceType.EMPTY);
        }
    }

    public void addConnectedPiece(Piece connectedPiece) {
        this.connectedPieces.add(connectedPiece);
    }

    public void addConnectedPiece(List<Piece> connectedPieces) {
        this.connectedPieces.addAll(connectedPieces);
    }

    public List<Piece> getConnectedPieces() {
        return connectedPieces;
    }

    public boolean isConnectedTo(Piece otherPiece) {
        return connectedPieces.contains(otherPiece);
    }

    @Override
    public int compareTo(Piece o) {
        return this.hashCode() - o.hashCode();
    }
}

