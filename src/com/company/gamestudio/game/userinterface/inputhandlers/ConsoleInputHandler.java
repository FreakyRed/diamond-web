package com.company.gamestudio.game.userinterface.inputhandlers;

import com.company.gamestudio.game.core.Field;
import com.company.gamestudio.game.core.GamePhase;
import com.company.gamestudio.game.core.Player;
import com.company.gamestudio.game.exceptions.gamephase.WrongGamePhaseException;
import com.company.gamestudio.game.exceptions.pieces.PiecesException;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleInputHandler implements InputHandler {

    private Field field;
    private Scanner scanner = new Scanner(System.in);
    private final Pattern INPUT_PATTERN_PLACEMENT = Pattern.compile("\\s*([A-U])([1-9])\\s*");
    private final Pattern INPUT_PATTERN_MOVEMENT = Pattern.compile("\\s*(M)(([A-U])([1-9]))(([A-U])([1-9]))\\s*");
    private final Pattern INPUT_PATTERN_REMOVE = Pattern.compile("\\s*(R)(([A-U])([1-9]))\\s*");

    public ConsoleInputHandler(Field field) {
        this.field = field;
    }

    @Override
    public void handleInput() {
        if (field.getGamePhase() == GamePhase.PLACEMENT) {
            handleInputForPlacementPhase();
        } else {
            handleInputForMovementPhase();
        }
    }

    private void handleInputForPlacementPhase() {
        System.out.println("\u001B[35mEnter coordinates for placement of your piece (e.g. A1, K3):\u001B[0m");
        String line = scanner.nextLine().toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }
        Matcher matcher = INPUT_PATTERN_PLACEMENT.matcher(line);

        if (matcher.matches()) {
            placePieceToPlace(matcher);
        } else {
            System.out.println("Bad input, please try again");
        }
    }

    private void placePieceToPlace(Matcher matcher) {
        int row = matcher.group(1).charAt(0) - 65;
        int col = Integer.parseInt(matcher.group(2)) - 1;

        String currentPlayer = field.getCurrentPlayer().toString() + "(" + getSymbolForPlayer() + ")";
        try {
            if (field.placePiece(row, col)) {
                System.out.println("Placed " + currentPlayer + " piece to " + matcher.group(1) + matcher.group(2));
            } else {
                System.out.println("Entered coordinates are not within the field");
            }
        } catch (PiecesException | WrongGamePhaseException e) {
            System.out.println(e.toString());
        }
    }

    private void handleInputForMovementPhase() {
        System.out.println("\u001B[35mChoose to remove red piece (e.g. RK1) or move your piece to a new position (e.g. MA1B2, MU1T2):\u001B[0m");
        String line = scanner.nextLine().toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }
        Matcher moveMatcher = INPUT_PATTERN_MOVEMENT.matcher(line);
        Matcher removeMatcher = INPUT_PATTERN_REMOVE.matcher(line);

        if (moveMatcher.matches()) {
            parseInputAndMovePiece(moveMatcher);
        } else if (removeMatcher.matches()) {
            removeNeutralPiece(removeMatcher);
        } else {
            System.out.println("Bad input, try again");
        }
    }

    private void parseInputAndMovePiece(Matcher matcher) {
        int rowFrom = matcher.group(3).charAt(0) - 65;
        int colFrom = Integer.parseInt(matcher.group(4)) - 1;
        int rowTo = matcher.group(6).charAt(0) - 65;
        int colTo = Integer.parseInt(matcher.group(7)) - 1;

        movePieceFromPlace(matcher, rowFrom, colFrom, rowTo, colTo);
    }

    private void movePieceFromPlace(Matcher matcher, int rowFrom, int colFrom, int rowTo, int colTo) {
        Player currentPlayer = field.getCurrentPlayer();
        try {
            if (field.movePiece(rowFrom, colFrom, rowTo, colTo)) {
                System.out.println("Moved " + currentPlayer.toString() + "(" + getSymbolForPlayer() + ")"
                        + " piece from " + matcher.group(2) + " to " + matcher.group(5));
            } else {
                System.out.println("Entered coordinates are not within the field");
            }
        } catch (PiecesException | WrongGamePhaseException e) {
            System.out.println(e.toString());
        }
    }

    private void removeNeutralPiece(Matcher matcher) {
        int row = matcher.group(3).charAt(0) - 65;
        int col = Integer.parseInt(matcher.group(4)) - 1;

        try {
            if (field.removeRedPiece(row, col)) {
                System.out.println("Removed neutral(red) piece from " + matcher.group(2));
            } else {
                System.out.println("Couldn't remove neutral(red) piece at " + matcher.group(2));
            }
        } catch (PiecesException e) {
            System.out.println(e.toString());
        }
    }

    private String getSymbolForPlayer() {
        switch (field.getCurrentPlayer()) {
            case BLACK:
                return ("\u001B[37m" + "&" + "\u001B[0m");
            case WHITE:
                return ("\u001B[30m" + "$" + "\u001B[0m");
            default:
                throw new IllegalStateException();
        }
    }

}
