package com.company;

import com.company.gamestudio.game.core.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Field field = new Field();
        field.printField();

        Piece[] pieces = field.getAllPieces();

        pieces[0].setConnectedPieces(Arrays.asList(pieces[1],pieces[2],pieces[3]));
        pieces[1].setConnectedPieces(Arrays.asList(pieces[0],pieces[3],pieces[4]));

        Triangle triangle = new Triangle(Arrays.asList(pieces[0],pieces[1],pieces[3]));
        Square square = new Square(Arrays.asList(pieces[2],pieces[4],pieces[5],pieces[8]));

        pieces[8].setPieceType(PieceType.RED);
        square.getPieces().stream()
                .map(Piece::getPieceType)
                .forEach(System.out::println);

        System.out.println(pieces[0].getConnectedPieces());
    }

}
