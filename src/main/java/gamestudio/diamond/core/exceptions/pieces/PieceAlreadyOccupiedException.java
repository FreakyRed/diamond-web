package gamestudio.diamond.core.exceptions.pieces;

public class PieceAlreadyOccupiedException extends PiecesException {
    @Override
    public String toString() {
        return "Piece at the entered position is already occupied";
    }
}
