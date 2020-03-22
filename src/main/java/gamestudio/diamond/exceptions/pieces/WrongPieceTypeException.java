package gamestudio.diamond.exceptions.pieces;

public class WrongPieceTypeException extends PiecesException {

    @Override
    public String toString() {
        return "Piece at entered coordinate is not the required colour";
    }
}
