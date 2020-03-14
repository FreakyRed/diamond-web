package com.company.gamestudio.game.userinterface;

import com.company.gamestudio.game.core.Field;
import com.company.gamestudio.game.core.GamePhase;
import com.company.gamestudio.game.core.GameState;
import com.company.gamestudio.game.exceptions.gamephase.WrongGamePhaseException;
import com.company.gamestudio.game.exceptions.pieces.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI implements UI {
    private Field field;
    private Scanner scanner = new Scanner(System.in);

    private final Pattern INPUT_PATTERN_PLACEMENT = Pattern.compile("\\s*([A-U])([1-9])\\s*");
    private final Pattern INPUT_PATTERN_MOVEMENT = Pattern.compile("\\s*(M)(([A-U])([1-9]))(([A-U])([1-9]))\\s*");
    private final Pattern INPUT_PATTERN_REMOVE = Pattern.compile("\\s*(R)(([A-U])([1-9]))\\s*");

    public ConsoleUI(Field field) {
        this.field = field;
    }

    @Override
    public void run() {
        //printInfoAboutGame();
        do {
            printField();
            processInput();
        }
        while (field.getGameState() == GameState.PLAYING);
        printField();
        printFinalMessage();
    }

    private void printFinalMessage() {
        if (field.getGameState() == GameState.SOLVED) {
            System.out.println("Congratulations " + field.getCurrentPlayer().toString() + " player won!");
        }
    }

    private void printField() {
        printGameInfo();
        printFieldBody();
    }

    private void printGameInfo() {
        System.out.println("Gamephase: " + field.getGamePhase() +
                "               " + "Current Player: " + field.getCurrentPlayer().toString());
    }

    private void printFieldBody() {
        System.out.println("=======================================================");
        field.printField();
        System.out.println("=======================================================");
    }

    private void processInput() {
        if (field.getGamePhase() == GamePhase.PLACEMENT) {
            processInputForPlacementPhase();
        } else {
            processInputForMovementPhase();
        }
    }

    private void processInputForPlacementPhase() {
        System.out.println("Enter coordinates for placement of your piece (e.g. A1, K3):");
        String line = scanner.nextLine().toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }
        Matcher m = INPUT_PATTERN_PLACEMENT.matcher(line);

        if (m.matches()) {
            parseInputAndPlacePiece(m);
        } else {
            System.out.println("Bad input, please try again");
        }
    }

    private void parseInputAndPlacePiece(Matcher m) {
        int row = m.group(1).charAt(0) - 65;
        int col = Integer.parseInt(m.group(2)) - 1;

        String currentPlayer = field.getCurrentPlayer().toString();
        try {
            if (field.placePiece(row, col)) {
                System.out.println("Placed " + currentPlayer + " piece to " + m.group(1) + m.group(2));
            } else {
                System.out.println("Entered coordinates are not within the field");
            }
        } catch (PiecesException | WrongGamePhaseException e) {
            System.out.println(e.toString());
        }
    }

    private void processInputForMovementPhase() {
        System.out.println("Choose to remove red piece (e.g. RK1) or move your to a new position (e.g. MA1B2, MU1T2):");
        String line = scanner.nextLine().toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }
        Matcher m = INPUT_PATTERN_MOVEMENT.matcher(line);
        Matcher r = INPUT_PATTERN_REMOVE.matcher(line);

        if (m.matches()) {
            parseInputForMovementAndMove(m);
        } else if (r.matches()) {
            parseInputForRemovingAndRemove(r);
        } else {
            System.out.println("Bad input, try again");
        }
    }

    private void parseInputForMovementAndMove(Matcher matcher) {
        int rowFrom = matcher.group(3).charAt(0) - 65;
        int colFrom = Integer.parseInt(matcher.group(4)) - 1;
        int rowTo = matcher.group(6).charAt(0) - 65;
        int colTo = Integer.parseInt(matcher.group(7)) - 1;

        tryToMoveAndPrintMessage(matcher, rowFrom, colFrom, rowTo, colTo);
    }

    private void tryToMoveAndPrintMessage(Matcher matcher, int rowFrom, int colFrom, int rowTo, int colTo) {
        String currentPlayer = field.getCurrentPlayer().toString();
        try {
            if (field.movePiece(rowFrom, colFrom, rowTo, colTo)) {
                System.out.println("Moved " + currentPlayer + " piece from " + matcher.group(2) + matcher.group(3) + " to " + matcher.group(5) + matcher.group(6));
            } else {
                System.out.println("Entered coordinates are not within the field");
            }
        } catch (PiecesException | WrongGamePhaseException e) {
            System.out.println(e.toString());
        }
    }

    private void parseInputForRemovingAndRemove(Matcher matcher) {
        int row = matcher.group(3).charAt(0) - 65;
        int col = Integer.parseInt(matcher.group(4)) - 1;

        if(field.removeRedPiece(row,col)) {
            System.out.println("Removed red piece from " + matcher.group(2));
        }
        else {
            System.out.println("Couldn't remove red piece at " + matcher.group(2) + ", try again");
        }
    }
}

