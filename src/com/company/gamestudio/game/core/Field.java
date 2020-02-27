package com.company.gamestudio.game.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Field {
    private Piece[][] field;
    private List<Tile> tiles;

    public Field() {
        field = new Piece[21][];
        initializeField();
    }

    private void initializeField() {
        createJaggedField();
        fillField();
    }

    private void createJaggedField() {
        int[] numberOfCols = new int[]{1, 2, 1, 2, 4, 2, 3, 6, 3, 4, 6, 4, 3, 6, 3, 2, 4, 2, 1, 2, 1};
        for (int i = 0; i < field.length; i++) {
            field[i] = new Piece[numberOfCols[i]];
        }
    }

    private void fillField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Piece();
            }
        }
    }

    public Piece[] getAllPieces() {
        return Stream.of(field)
                .flatMap(Stream::of)
                .toArray(Piece[]::new);
    }

    public void printField() {
        for (Piece[] pieceRow : field) {
            for (Piece piece : pieceRow) {
                //if (piece.getPieceType() == PieceType.EMPTY) {
                //  drawLine(pieceRow.length);
                //}
                System.out.println(piece.getPieceType());
            }
            System.out.println();
        }
    }


    private void drawLine(int size) {

    }

    public Piece[][] getField() {
        return this.field;
    }

}
    /*
    public boolean placePiece(int x, int y) {
        if (this.field[x][y].getPieceType() != PieceType.EMPTY) {
            return false;
        }
        switch (currentPlayer()) {
            case Player.BLACK:
                this.field[x][y].setPieceType(PieceType.BLACK);
                break;
            case Player.WHITE:
                this.field[x][y].setPieceType(PieceType.WHITE);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean movePiece(int xFrom, int yFrom, int xTo, int yTo) {
        if (this.field[xFrom][yFrom].getPieceType() == PieceType.EMPTY) {
            return false;
        }

        this.field[xFrom][yFrom].setPieceType(PieceType.EMPTY);
        if (currentPlayer() == Player.Black) {
            this.field[xTo][yTo].setPieceType(PieceType.BLACK);
        } else {
            this.field[xTo][yTo].setPieceType(PieceType.WHITE);
        }
        return true;
    }
}
*/