package sk.tuke.gamestudio.game.diamond.frajkor.userinterface;

import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Piece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.PieceType;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase.WrongGamePhaseException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;

import java.util.Arrays;

public class EasyBot extends Bot {
    public EasyBot(Field field) {
        super(field);
    }

    @Override
    public void placePiece() {
        try {
            int row, col;
            do {
                row = random.nextInt(field.getField().length);
                col = random.nextInt(field.getField()[row].length);
            }
            while (!field.placePiece(row, col));
        } catch (WrongGamePhaseException e) {
            /*DO NOTHING*/
        }
    }

    @Override
    public void movePiece() {
        int rowFrom = 0, colFrom = 0, rowTo = 0, colTo = 0;

        Piece piece = getRandomPiece();
        Piece connectedEmptyPiece = getRandomConnectedPieceTo(piece);

        for (int row = 0; row < field.getField().length; row++) {
            for (int col = 0; col < field.getField()[row].length; col++) {
                if (piece == field.getField()[row][col]) {
                    colFrom = col;
                    rowFrom = row;
                } else if (connectedEmptyPiece == field.getField()[row][col]) {
                    colTo = col;
                    rowTo = row;
                }
            }
            tryToMove(rowFrom,colFrom,rowTo,colTo);
        }
    }

    private Piece getRandomPiece(){
        return Arrays.stream(field.getAllPieces()).filter(p -> p.getPieceType() == PieceType.WHITE)
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
            field.movePiece(rowFrom, colFrom, rowTo, colTo);
        } catch (PiecesException | WrongGamePhaseException exception) {
            //DO NOTHING
        }
    }
}
