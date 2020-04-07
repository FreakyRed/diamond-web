package gamestudio.game.diamond.frajkor.core.exceptions.pieces;

public class NeutralPieceAdjacentException extends PiecesException {
    @Override
    public String toString() {
        return "There are player pieces adjacent to this neutral piece.";
    }
}
