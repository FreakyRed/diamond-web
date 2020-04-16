package sk.tuke.gamestudio.game.diamond.frajkor.userinterface.inputhandlers;

import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;
import sk.tuke.gamestudio.game.diamond.frajkor.core.GamePhase;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Piece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.PieceType;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase.WrongGamePhaseException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;


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
        int rowFrom = 0, colFrom = 0, rowTo = 0, colTo = 0;

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
            tryToMove(rowFrom,colFrom,rowTo,colTo);
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

    private void tryToMove(int rowFrom, int colFrom, int rowTo, int colTo){
        try {
            getField().movePiece(rowFrom, colFrom, rowTo, colTo);
            System.out.println(BOT_NAME + " moved piece from " +
                    (char) (rowFrom + 'A') + (colFrom + 1) + " to " + (char) (rowTo + 'A') + (colTo + 1));
        } catch (WrongGamePhaseException e) {
            System.out.println("Wrong gamephase for movement command");
        } catch (PiecesException e) {
            //DO NOTHING
        }
    }

}
