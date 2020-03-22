package main.java.gamestudio.game.core;

import main.java.gamestudio.game.exceptions.gamephase.WrongGamePhaseException;
import main.java.gamestudio.game.exceptions.pieces.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Field {
    private Piece[][] field = new Piece[21][];
    private List<Tile> tiles;
    private GamePhase gamePhase = GamePhase.PLACEMENT;
    private GameState gameState = GameState.PLAYING;
    private Player currentPlayer = Player.BLACK;
    private static int turnsWithoutCaptureOrRemove = 0;

    private static final int MAX_PIECES = 24;

    public Field() {
        initializeField();
        tiles = new Tiles(getAllPieces()).getTileList();
    }

    private void initializeField() {
        createJaggedField();
        fillField();
    }

    private void createJaggedField() {
        int[] numberOfCols = new int[]{1, 2, 1, 2, 4, 2, 3, 6, 3, 4, 6, 4, 3, 6, 3, 2, 4, 2, 1, 2, 1};
        for (int i = 0; i < field.length; i++) {
            field[i] = new Piece[numberOfCols[i]];
        }
    }

    private void fillField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Piece();
            }
        }
    }

    public Piece[] getAllPieces() {
        return Stream.of(field)
                .flatMap(Stream::of)
                .toArray(Piece[]::new);
    }

    public List<Tile> getTiles(){
        return this.tiles;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Piece[][] getField() {
        return this.field;
    }

    public boolean placePiece(int row, int col) throws PiecesException, WrongGamePhaseException {
        if (gamePhase != GamePhase.PLACEMENT) {
            throw new WrongGamePhaseException();
        }

        if (row < 0 || row >= field.length || col < 0 || col >= field[row].length) {
            return false;
        }

        if (this.field[row][col].getPieceType() != PieceType.EMPTY) {
            throw new PieceAlreadyOccupiedException();
        }

        if (getCurrentPlayer() == Player.BLACK) {
            this.field[row][col].setPieceType(PieceType.BLACK);
        } else {
            this.field[row][col].setPieceType(PieceType.WHITE);
        }

        if (isSolved()) {
            this.gameState = GameState.SOLVED;
            return true;
        }

        changePlayer();
        transitionBetweenPhases();
        return true;
    }

    private boolean isSolved() {
        return tiles.stream()
                .filter(t -> t instanceof Square)
                .anyMatch(t -> ((Square) t).filledWithOneColour(getCurrentPlayer().getColour()));
    }

    private void capturePieceWithinTriangle(Piece piece) {
        List<Triangle> triangleList = (List<Triangle>) findTrianglesContainingPiece(piece);
        for (Triangle triangle : triangleList) {
            if (triangle.isCapturable(getCurrentPlayer())) {
                triangle.findCapturablePiece(getCurrentPlayer()).capture();
                turnsWithoutCaptureOrRemove = 0;
            } else {
                turnsWithoutCaptureOrRemove += 1;
            }
        }
    }

    private List<? extends Tile> findTrianglesContainingPiece(Piece piece) {
        return tiles.stream()
                .filter(t -> t instanceof Triangle)
                .filter(t -> t.getPieces().contains(piece))
                .collect(Collectors.toList());
    }

    public boolean movePiece(int rowFrom, int colFrom, int rowTo, int colTo) throws
            PiecesException, WrongGamePhaseException {
        if (this.gamePhase != GamePhase.MOVEMENT) {
            throw new WrongGamePhaseException();
        }

        if (areCoordinatesOutOfBounds(rowFrom, colFrom, rowTo, colTo)) {
            return false;
        }
        checkPiecesExceptions(rowFrom, colFrom, rowTo, colTo);

        this.field[rowFrom][colFrom].setPieceType(PieceType.EMPTY);
        if (getCurrentPlayer() == Player.BLACK) {
            this.field[rowTo][colTo].setPieceType(PieceType.BLACK);
        } else {
            this.field[rowTo][colTo].setPieceType(PieceType.WHITE);
        }

        capturePieceWithinTriangle(field[rowTo][colTo]);
        if (isSolved()) {
            this.gameState = GameState.SOLVED;
            return true;
        }
        changePlayer();
        return true;
    }

    private void transitionBetweenPhases() {
        long placedPieces = Arrays.stream(getAllPieces()).filter(p -> p.getPieceType() != PieceType.EMPTY).count();

        if (placedPieces == MAX_PIECES) {
            this.gamePhase = GamePhase.MOVEMENT;
        }
    }

    public boolean removeRedPiece(int row, int col) throws PiecesException {
        if (row < 0 || row >= field.length || col < 0 || col >= field[row].length) {
            return false;
        }

        if (field[row][col].removeRedPiece()) {
            turnsWithoutCaptureOrRemove = 0;
            changePlayer();
            return true;
        }
        return false;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public GameState getGameState() {
        return gameState;
    }

    private void changePlayer() {
        currentPlayer = (currentPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
    }

    public boolean areDrawConditionsMet() {
        if (this.gamePhase != GamePhase.MOVEMENT) {
            return false;
        }
        if (turnsWithoutCaptureOrRemove == 50) {
            return true;
        }
        return !isMovePossible();
    }

    private boolean isMovePossible() {
        return Arrays.stream(getAllPieces())
                .filter(p -> p.getPieceType() == getCurrentPlayer().getColour())
                .anyMatch(p -> p.getConnectedPieces().stream().anyMatch(r -> r.getPieceType() == PieceType.EMPTY));
    }

    private boolean areCoordinatesOutOfBounds(int rowFrom, int colFrom, int rowTo, int colTo) {
        if (rowFrom < 0 || rowFrom >= field.length || colFrom < 0 || colFrom >= field[rowFrom].length) {
            return true;
        }
        return rowTo < 0 || rowTo >= field.length || colTo < 0 || colTo >= field[rowTo].length;
    }

    private void checkPiecesExceptions(int rowFrom, int colFrom, int rowTo, int colTo) throws PiecesException {
        if (this.field[rowFrom][colFrom].getPieceType() == PieceType.EMPTY) {
            throw new PieceNotPresentException();
        }
        if (this.field[rowTo][colTo].getPieceType() != PieceType.EMPTY) {
            throw new PieceAlreadyOccupiedException();
        }
        if (!this.field[rowFrom][colFrom].isConnectedTo(this.field[rowTo][colTo])) {
            throw new PiecesNotConnectedException();
        }
        if (this.field[rowFrom][colFrom].getPieceType() != getCurrentPlayer().getColour()) {
            throw new WrongPieceTypeException();
        }
    }

    public List<Piece> getConnectedPieces(int row, int col){
        return field[row][col].getConnectedPieces();
    }
}
