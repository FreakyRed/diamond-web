package sk.tuke.gamestudio.game.diamond.frajkor.core;

import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.NeutralPieceAdjacentException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.WrongPieceTypeException;

import java.util.ArrayList;
import java.util.List;

public class Piece  extends IPiece implements Comparable<Piece> {

    private PieceType pieceType = PieceType.EMPTY;
    private List<Piece> connectedPieces = new ArrayList<>();

    public Piece() {

    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    void capture() {
        if (this.pieceType == PieceType.BLACK || this.pieceType == PieceType.WHITE) {
            this.setPieceType(PieceType.NEUTRAL);
        }
    }

    boolean isSurroundedByEnemyPieces() {
        if (pieceType == PieceType.BLACK) {
            return this.getConnectedPieces().stream()
                    .map(Piece::getPieceType)
                    .allMatch(p -> p == PieceType.WHITE);
        } else if (pieceType == PieceType.WHITE) {
            return this.getConnectedPieces().stream()
                    .map(Piece::getPieceType)
                    .allMatch(p -> p == PieceType.BLACK);
        }

        return false;
    }

    boolean removeRedPiece() throws PiecesException {
        if (this.pieceType != PieceType.NEUTRAL) {
            throw new WrongPieceTypeException();
        }
        if (getConnectedPieces().stream()
                .anyMatch(p -> p.getPieceType() == PieceType.WHITE || p.getPieceType() == PieceType.BLACK)) {
            throw new NeutralPieceAdjacentException();
        } else {
            this.setPieceType(PieceType.EMPTY);
            return true;
        }
    }

    boolean isNeutralPieceRemovable(){
        if(this.getPieceType() == PieceType.NEUTRAL) {
           return isSurroundedByEnemyPieces();
        }
        return false;
    }

    void addConnectedPiece(Piece connectedPiece) {
        this.connectedPieces.add(connectedPiece);
    }

    public List<Piece> getConnectedPieces() {
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

