package com.company.gamestudio.game.core;

import java.util.*;

class Tiles {
    private List<Tile> tileList = new ArrayList<>();
    private Piece[] pieces;

    Tiles(Piece[] pieces) {
        this.pieces = pieces;
        setup();
    }

    private void setup() {
        createConnectionsBetweenPieces();
        tileList.addAll(createSquareTiles());
        tileList.addAll(createTriangleTiles());
    }

    private void createConnectionsBetweenPieces() {
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

    private List<Square> createSquareTiles() {
        String connections = "1-3-4-7 2-3-5-8 6-10-12-16 7-10-13-17 8-11-13-18 9-11-14-19 15-21-24-28" +
                " 16-21-25-29 17-22-25-30 18-22-26-31 19-23-26-32 20-23-27-33";

        String[] allConnectionsSeparated = connections.split(" ");
        String[][] allConnectionsSeparated2D = new String[allConnectionsSeparated.length][];

        for (int i = 0; i < allConnectionsSeparated.length; i++) {
            allConnectionsSeparated2D[i] = allConnectionsSeparated[i].split("-");
        }

        List<Square> listOfSquares = new ArrayList<>();

        for (String[] strings : allConnectionsSeparated2D) {
            Square square = new Square();
            Square square2 = new Square();
            for (String string : strings) {
                square.addPiece(pieces[Integer.parseInt(string)]);
                square2.addPiece(pieces[pieces.length - 1 - Integer.parseInt(string)]);
            }
            listOfSquares.add(square);
            listOfSquares.add(square2);
        }
        return listOfSquares;
    }

    private List<Triangle> createTriangleTiles() {
        HashSet<List<Piece>> triangleValues = new HashSet<>();
        for (Piece piece : pieces) {
            boolean broken = false;
            for (int j = 0; j < piece.getConnectedPieces().size(); j++) {
                for (int k = 0; k < piece.getConnectedPieces().get(j).getConnectedPieces().size(); k++) {
                    if (piece.isConnectedTo(piece.getConnectedPieces().get(j).getConnectedPieces().get(k))) {
                        List<Piece> pieceList = Arrays.asList(piece, piece.getConnectedPieces().get(j),
                                piece.getConnectedPieces().get(j).getConnectedPieces().get(k));

                        pieceList.sort(Piece::compareTo);

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

    private List<Triangle> createAllTriangles(HashSet<List<Piece>> setOfTriangles) {
        List<Triangle> triangleList = new ArrayList<>();
        for (List<Piece> list : setOfTriangles) {
            triangleList.add(createTriangle(list));
        }
        return triangleList;
    }

    private Triangle createTriangle(List<Piece> listOfPieces) {
        return new Triangle(listOfPieces);
    }

    List<Tile> getTileList() {
        return tileList;
    }
}


