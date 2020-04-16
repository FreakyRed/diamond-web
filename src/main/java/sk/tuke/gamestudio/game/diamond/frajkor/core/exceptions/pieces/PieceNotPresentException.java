package sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces;

public class PieceNotPresentException extends PiecesException {
    @Override
    public String toString() {
        return "Piece at entered position is not present, therefore you cannot move it.";
    }
}
