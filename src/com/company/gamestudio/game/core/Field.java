package com.company.gamestudio.game.core;

import com.company.gamestudio.game.core.players.BlackPlayer;
import com.company.gamestudio.game.core.players.Player;
import com.company.gamestudio.game.core.players.WhitePlayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Field {
    private Piece[][] field;
    private List<Tile> tiles = new ArrayList<>();
    private GamePhase gamePhase = GamePhase.Placement;
    private Player[] players = new Player[]{new WhitePlayer(),new BlackPlayer()};
    //private Player player;

    public Field() {
        field = new Piece[21][];
        initializeField();
    }

    private void initializeField() {
        createJaggedField();
        fillField();
        setupConnections();
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

    private Player getCurrentPlayer(){
        return players[0].getRemainingPieces() <= players[1].getRemainingPieces() ? players[0] : players[1];
    }

    private void setupConnections(){
        createConnectionsBetweenPieces(getAllPieces());
        tiles.addAll(createConnectedSquares(getAllPieces()));
        tiles.addAll(createConnectedTriangles(getAllPieces()));
    }

    private List<Square> createConnectedSquares(Piece[] pieces){
        String connections = "1-3-4-7 2-3-5-8 6-10-12-16 7-10-13-17 8-11-13-18 9-11-14-19 15-21-24-28" +
                " 16-21-25-29 17-22-25-30 18-22-26-31 19-23-26-32 20-23-27-33";

        String[] allConnectionsSeparated = connections.split(" ");
        String[][] allConnectionsSeparated2D = new String[allConnectionsSeparated.length][];

        for (int i = 0; i < allConnectionsSeparated.length; i++) {
            allConnectionsSeparated2D[i] = allConnectionsSeparated[i].split("-");
        }

        List<Square> listOfSquares = new ArrayList<>();

        for(int i = 0;i<allConnectionsSeparated2D.length;i++){
            Square square = new Square();
            Square square2 = new Square();
            for(int j=0;j<allConnectionsSeparated2D[i].length;j++){
                square.addPiece(pieces[Integer.parseInt(allConnectionsSeparated2D[i][j])]);
                square2.addPiece(pieces[pieces.length - 1 - Integer.parseInt(allConnectionsSeparated2D[i][j])]);
            }
            listOfSquares.add(square);
            listOfSquares.add(square2);
        }
        return listOfSquares;
    }

    private List<Triangle> createConnectedTriangles(Piece[] pieces){
        HashSet<List<Piece>> triangleValues = new HashSet<>();
        for (int i = 0; i < pieces.length; i++) {
            boolean broken = false;
            for (int j = 0; j < pieces[i].getConnectedPieces().size(); j++) {
                for (int k = 0; k < pieces[i].getConnectedPieces().get(j).getConnectedPieces().size(); k++) {
                    if (pieces[i].isConnectedTo(pieces[i].getConnectedPieces().get(j).getConnectedPieces().get(k))) {
                        List<Piece> pieceList = Arrays.asList(pieces[i], pieces[i].getConnectedPieces().get(j),
                                pieces[i].getConnectedPieces().get(j).getConnectedPieces().get(k));
                        Collections.sort(pieceList, Piece::compareTo);
                        if (!triangleValues.contains(pieceList)) {
                            triangleValues.add(pieceList);
                            broken = true;
                        }
                    }
                }
                if (broken) { break; }
            }
        }
        return createAllTriangles(triangleValues);
    }

    private Triangle createTriangle(List<Piece> listOfPieces){
        return new Triangle(listOfPieces);
    }

    private List<Triangle> createAllTriangles(HashSet<List<Piece>> setOfTriangles){
        List<Triangle> triangleList = new ArrayList<>();
        for(List<Piece> list : setOfTriangles){
            triangleList.add(createTriangle(list));
        }
        return triangleList;
    }

    public void addTiles(List<? extends Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    private void createConnectionsBetweenPieces(Piece[] pieces) {
        String connections = "1-2-3 0-3-4 0-3-5 0-1-2-7-8 1-6-7-10 2-8-9-11 4-10-12 3-4-8-10-13 3-5-7-11-13 5-11-14" +
                " 4-6-7-16-17 5-8-9-18-19 6-15-16-21 7-8-17-18-22 9-19-20-23 12-21-24 10-12-17-21-25 10-13-16-22-18-25 11-13-22-19-26" +
                " 11-14-18-23-26 14-23-27 12-15-16-28-29 13-17-18-30-31 14-19-20-32-33 15-28-34 16-17-29-30-35 18-19-31-32-36" +
                " 20-33-37 24-21-29-34-38 21-28-25-35-38 25-22-31-39-35";

        String[] allConnectionsSeparated = connections.split(" ");
        String[][] allConnectionsSeparated2D = new String[allConnectionsSeparated.length][];

        for (int i = 0; i < allConnectionsSeparated.length; i++) {
            allConnectionsSeparated2D[i] = allConnectionsSeparated[i].split("-");
        }

        for (int i = 0; i < allConnectionsSeparated2D.length; i++) {
            for (int j = 0; j < allConnectionsSeparated2D[i].length; j++) {
                pieces[i].addConnectedPiece(pieces[Integer.parseInt(allConnectionsSeparated2D[i][j])]);
                pieces[pieces.length - 1 - i]
                        .addConnectedPiece(pieces[pieces.length - 1 - Integer.parseInt(allConnectionsSeparated2D[i][j])]);
            }
        }
    }

    public void printField() {
        for (Piece[] pieceRow : field) {
            for (Piece piece : pieceRow) {
                System.out.print(getPrefix(pieceRow.length));
                switch (piece.getPieceType()) {
                    case EMPTY:
                        System.out.print("E");
                        break;
                    case RED:
                        System.out.print("R");
                        break;
                    case BLACK:
                        System.out.print("B");
                        break;
                    case WHITE:
                        System.out.print("W");
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            System.out.println();
        }
    }

    private String getPrefix(int number) {
        String string = "";
        for (int i = 0; i < 10 / number; i++) {
            string += " ";
        }
        return string;
    }

    public Piece[][] getField() {
        return this.field;
    }

    public boolean placePiece(int row, int col, PieceType type) {
        if (row < 0 || row >= field.length || col < 0 || col >= field[row].length) {
            return false;
        }

        if (this.field[row][col].getPieceType() != PieceType.EMPTY) {
            return false;
        }

        switch (type) {
            case BLACK:
                this.field[row][col].setPieceType(PieceType.BLACK);
                break;
            case WHITE:
                this.field[row][col].setPieceType(PieceType.WHITE);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean isSolved() {
        return tiles.stream()
                .filter(t -> t instanceof Square)
                .anyMatch(t -> ((Square) t).filledWithOneColour(PieceType.WHITE));
    }

    public void  triangleCapture(Piece piece){
        List<Triangle> triangleList = (List<Triangle>) findTrianglesContainingPiece(piece);
        for(Triangle triangle : triangleList){
            if(triangle.isCapturable(getCurrentPlayer())) {
                triangle.findCapturablePiece(getCurrentPlayer()).capture();
            }
        }
    }

    private List<? extends Tile> findTrianglesContainingPiece(Piece piece){
        return tiles.stream()
                .filter(t -> t instanceof Triangle)
                .filter(t -> t.getPieces().contains(piece))
                .collect(Collectors.toList());
    }

    public void addTile(Tile tile) {
        if (!this.tiles.contains(tile)) {
            this.tiles.add(tile);
        }
    }

    public boolean movePiece(int xFrom, int yFrom, int xTo, int yTo) {
        if (this.field[xFrom][yFrom].getPieceType() == PieceType.EMPTY) {
            return false;
        }

        this.field[xFrom][yFrom].setPieceType(PieceType.EMPTY);
        if (getCurrentPlayer() instanceof BlackPlayer) {
            this.field[xTo][yTo].setPieceType(PieceType.BLACK);
        } else {
            this.field[xTo][yTo].setPieceType(PieceType.WHITE);
        }
        triangleCapture(field[xTo][yTo]);

        return true;
    }
}
