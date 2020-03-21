package main.java.gamestudio.game.exceptions.pieces;

public class PiecesNotConnectedException extends PiecesException {
    @Override
    public String toString() {
        return "Pieces at entered coordinates are not connected";
    }
}
