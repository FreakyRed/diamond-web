package gamestudio.game.diamond.frajkor.core;

import java.util.ArrayList;
import java.util.List;

public class Triangle extends Tile {

    private List<Piece> pieces = new ArrayList<>();

    public Triangle() {

    }

    Triangle(List<Piece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public List<Piece> getPieces() {
        return pieces;
    }

    @Override
    protected void addPiece(Piece piece) {
        this.pieces.add(piece);
    }

    boolean isCapturable(Player player) {
        int countBlack = 0, countWhite = 0;
        for (Piece piece : pieces) {
            if (piece.getPieceType() == PieceType.BLACK) {
                countBlack++;
            }
            if (piece.getPieceType() == PieceType.WHITE) {
                countWhite++;
            }
        }

        if ((countBlack == 2 && countWhite == 1 && player == Player.BLACK) ||
                (countBlack == 1 && countWhite == 2 && player == Player.WHITE)) {
            return true;
        }

        return pieces.stream()
                .anyMatch(Piece::isSurroundedByEnemyPieces);
    }

    Piece findCapturablePiece(Player player) {
        if (pieces.stream().anyMatch(Piece::isSurroundedByEnemyPieces)) {
            return pieces.stream()
                    .filter(Piece::isSurroundedByEnemyPieces)
                    .findFirst()
                    .get();
        }

        return pieces.stream()
                .filter(p -> p.getPieceType() != player.getColour())
                .findFirst()
                .get();
    }

}
