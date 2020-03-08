package com.company;

import com.company.gamestudio.game.core.*;
import com.company.gamestudio.game.core.players.BlackPlayer;
import com.company.gamestudio.game.core.players.WhitePlayer;
import com.sun.source.tree.WhileLoopTree;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Field field = new Field();

        field.placePiece(0, 0, PieceType.WHITE);
        field.placePiece(20, 0, PieceType.BLACK);
        field.placePiece(20, 0, PieceType.WHITE);
        field.printField();

        Piece[] pieces = field.getAllPieces();

        Triangle triangle = new Triangle(Arrays.asList(pieces[0], pieces[1], pieces[3]));
        field.addTile(triangle);
        Triangle triangle1 = new Triangle(Arrays.asList(pieces[0], pieces[2], pieces[3]));
        field.addTile(triangle1);

        pieces[0].setPieceType(PieceType.WHITE);
        pieces[1].setPieceType(PieceType.BLACK);
        pieces[2].setPieceType(PieceType.WHITE);
        field.printField();
        field.movePiece(1, 1, 2, 0);
        field.printField();


        if (triangle.isCapturable(new WhitePlayer())) {
            System.out.println("CAPTURABLE");
        } else {
            System.out.println("NOT CAPTURABLE");
        }

        if (field.isSolved()) {
            System.out.println("SOLVED GAME OVER");
        } else {
            System.out.println("Game not over.");
        }

        /*
        pieces[8].setPieceType(PieceType.RED);
        square.getPieces().stream()
                .map(Piece::getPieceType)
                .forEach(System.out::println);

        System.out.println(pieces[0].getConnectedPieces());

       */


    }
}
