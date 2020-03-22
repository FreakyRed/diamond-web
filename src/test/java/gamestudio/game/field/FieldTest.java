package test.java.gamestudio.game.field;

import main.java.gamestudio.game.core.*;
import main.java.gamestudio.game.exceptions.gamephase.WrongGamePhaseException;
import main.java.gamestudio.game.exceptions.pieces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    private Field field = new Field();
    Random random = new Random();
    private final static int ALL_PIECES = 62;
    private final static int ALL_TILES = 72;
    private final static int COUNT_SQUARES = 24;
    private final static int COUNT_TRIANGLES = 48;


    public FieldTest() {
    }


    @Test
    public void checkGeneratedPiecesCountAtInitialization() {
        long count = Stream.of(field.getField())
                .flatMap(Stream::of)
                .count();

        assertEquals(count, ALL_PIECES, "Field was not initialized properly, wrong number of neutral pieces");
    }

    @Test
    public void checkTilesGeneratedCountAtInitialization() {
        assertEquals(field.getTiles().size(), ALL_TILES, "Field does not contain correct number of tiles");
    }

    @Test
    public void checkCorrectNumberOfSquaresGenerated() {
        long count = field.getTiles().stream()
                .filter(t -> t instanceof Square)
                .count();

        assertEquals(count, COUNT_SQUARES, "Field did not generate corrct amout of Squares for pieces to be connected");
    }

    @Test
    public void checkCorrectNumberOfTrianglesGenerated() {
        long count = field.getTiles().stream()
                .filter(t -> t instanceof Triangle)
                .count();

        assertEquals(count, COUNT_TRIANGLES, "Field did not generate corrct amout of Squares for pieces to be connected");
    }

    @Test
    public void checkCorrectPlacementOfPlaceMethod() throws PiecesException, WrongGamePhaseException {

        int row = random.nextInt(25);
        int col = random.nextInt(9);


        Player currentPlayer = field.getCurrentPlayer();

        if (row < 0 || row >= field.getField().length || col < 0 || col >= field.getField()[row].length) {
            assertFalse(field.placePiece(row, col));
        } else {
            field.placePiece(row, col);
            assertTrue(field.getField()[row][col].getPieceType() == currentPlayer.getColour());
        }
    }

    @Test
    public void checkWrongPhaseForMoveMethod() throws WrongGamePhaseException, PiecesException {
        int rowFrom = random.nextInt(25);
        int colFrom = random.nextInt(9);
        int rowTo = random.nextInt(25);
        int colTo = random.nextInt(9);

        assertThrows(WrongGamePhaseException.class, () -> {
            field.movePiece(rowFrom, colFrom, rowTo, colTo);
        });
    }

    @Test
    public void checkCorrectWorkOfRemovingNeutralPiece() {
        int rowFrom = 2, colFrom = 1, rowTo = 25, colTo = 1;

        assertThrows(WrongGamePhaseException.class, () -> {
            field.movePiece(rowFrom, colFrom, rowTo, colTo);
        });
    }

    @Test
    public void checkWhetherTheGameHasBeenSolved() throws PiecesException, WrongGamePhaseException {
        assertFalse(field.getGameState() == GameState.SOLVED);

        field.placePiece(1, 0);
        field.placePiece(11, 1);
        field.placePiece(2, 0);
        field.placePiece(10, 1);
        field.placePiece(3, 0);
        field.placePiece(12, 1);
        field.placePiece(4, 1);
        field.placePiece(13, 1);

        assertTrue(field.getGameState() == GameState.SOLVED);
    }

    @Test
    public void checkWhetherDrawConditionsAreMetAfterInitialization() {
        assertFalse(field.areDrawConditionsMet());
    }

    @Test
    public void checkIfRemovingNeutralPieceFromNothinThrowsException() throws PiecesException {
        int row = random.nextInt(25);
        int col = random.nextInt(9);

        if (row < 0 || row >= field.getField().length || col < 0 || col >= field.getField()[row].length) {
            assertFalse(field.removeRedPiece(row, col));
        } else {
            assertThrows(WrongPieceTypeException.class, () -> {
                field.removeRedPiece(row, col);
            });
        }
    }

    /*
    @Test
    public void checkCorrectFunctioningOfMoveInMovementPhase() throws WrongGamePhaseException, PiecesException {

        while (field.getGamePhase() != GamePhase.MOVEMENT) {
            int row = random.nextInt(25);
            int col = random.nextInt(9);

            if (row < 0 || row >= field.getField().length || col < 0 || col >= field.getField()[row].length) {
                continue;
            }

            if (!field.placePiece(row, col)) {
                continue;
            }
            field.placePiece(row, col);
        }

        Arrays.stream(field.getAllPieces())
                .filter(p -> p.getPieceType() == field.getCurrentPlayer().getColour())
                .filter(p -> p.getConnectedPieces().stream().filter(p -> p.getPieceType() == PieceType.NEUTRAL));
    }
}
*/
}


