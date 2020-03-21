package main.java.gamestudio.game.exceptions.pieces;

public class WrongPieceTypeException extends PiecesException {

    @Override
    public String toString() {
        return "Piece at entered coordinate is not the required colour";
    }
}
