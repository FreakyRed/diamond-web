package gamestudio.diamond.exceptions.pieces;

public class PiecesNotConnectedException extends PiecesException {
    @Override
    public String toString() {
        return "Pieces at entered coordinates are not connected";
    }
}
