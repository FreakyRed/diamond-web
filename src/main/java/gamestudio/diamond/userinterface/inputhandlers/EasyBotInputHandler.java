package gamestudio.diamond.userinterface.inputhandlers;

import gamestudio.diamond.core.Field;
import gamestudio.diamond.core.GamePhase;
import gamestudio.diamond.core.Piece;
import gamestudio.diamond.core.PieceType;
import gamestudio.diamond.core.exceptions.gamephase.WrongGamePhaseException;
import gamestudio.diamond.core.exceptions.pieces.PiecesException;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EasyBotInputHandler extends InputHandler {
    private Random random = new Random();
    private final String BOT_NAME = "\u001B[34mEasyBot\u001B[0m";

    public EasyBotInputHandler(Field field) {
        super(field);
    }


    private void placePiece() {
        try {
            int row, col;
            do {
                row = random.nextInt(getField().getField().length);
                col = random.nextInt(getField().getField()[row].length);
            }
            while (!getField().placePiece(row, col));
            System.out.println(BOT_NAME + " has placed piece to " + (char) (row + 'A') + (col + 1));
        } catch (WrongGamePhaseException e) {
            System.out.println("Wrong gamephase for placement command");
        }
    }


    private void movePiece() {
        try {
            int rowFrom = 0, colFrom = 0, rowTo = 0, colTo = 0;
            getCoordinatesOfPiecesToMove(rowFrom,colFrom,rowTo,colTo);
            getField().movePiece(rowFrom, colFrom, rowTo, colTo);
            System.out.println(BOT_NAME + " moved piece from " +
                    (char) (rowFrom + 'A') + (colFrom + 1) + "to " + (char) (rowTo + 'A') + (colTo + 1));
        } catch (WrongGamePhaseException e) {
            System.out.println("Wrong gamephase for movement command");
        } catch (PiecesException e) {
            //ignore
        }


    }

    @Override
    public void handleInput() {
        if (getField().getGamePhase() == GamePhase.PLACEMENT) {
            placePiece();
        } else {
            movePiece();
        }
    }

    @Override
    void proposeDraw() {
        setDrawnByPlayer(true);
    }


    @Override
    public void findPositionOfPiecesInField(List<Piece> pieces, StringBuilder stringBuilder) {
        //DO NOTHING
    }

    private Piece getRandomPiece(){
        return Arrays.stream(getField().getAllPieces()).filter(p -> p.getPieceType() == PieceType.WHITE)
                .filter(p -> p.getConnectedPieces().stream().anyMatch(piece1 -> piece1.getPieceType() == PieceType.EMPTY))
                .findFirst()
                .get();
    }

    private Piece getRandomConnectedPieceTo(Piece piece){
       return piece.getConnectedPieces().stream()
                .filter(p -> p.getPieceType() == PieceType.EMPTY)
                .findFirst()
                .get();
    }

    private void getCoordinatesOfPiecesToMove(int rowFrom, int colFrom, int rowTo, int colTo){
        Piece piece = getRandomPiece();
        Piece connectedEmptyPiece = getRandomConnectedPieceTo(piece);

        for (int row = 0; row < getField().getField().length; row++) {
            for (int col = 0; col < getField().getField()[row].length; col++) {
                if (piece == getField().getField()[row][col]) {
                    colFrom = col;
                    rowFrom = row;
                } else if (connectedEmptyPiece == getField().getField()[row][col]) {
                    colTo = col;
                    rowTo = row;
                }
            }
        }
    }

}
