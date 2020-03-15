package com.company.gamestudio.game.userinterface;

import com.company.gamestudio.game.core.Field;
import com.company.gamestudio.game.core.GamePhase;
import com.company.gamestudio.game.core.GameState;
import com.company.gamestudio.game.core.Player;
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
        printInfoAboutGame();
        do {
            printField();
            processInput();
        }
        while (field.getGameState() == GameState.PLAYING && !field.areDrawConditionsMet());
        printField();
        printFinalMessage();
        playAgain();
    }

    private void printFinalMessage() {
        if (field.areDrawConditionsMet()) {
            System.out.println("The game has been drawn. No captures or removes in last 50 turns, or no move was possible.");
        }

        if (field.getGameState() == GameState.SOLVED) {
            System.out.println("Congratulations " + field.getCurrentPlayer().toString() + " player won!");
        }
    }

    private void printField() {
        printGameInfo();
        printFieldBody();
    }

    private void printGameInfo() {
        System.out.print("Gamephase: " + field.getGamePhase() +
                "          " + "Current Player: " + field.getCurrentPlayer().toString());
        System.out.print("(" + field.getCurrentPlayer().getSymbol() + ")\n");
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

        String currentPlayer = field.getCurrentPlayer().toString() + "(" + field.getCurrentPlayer().getSymbol() + ")";
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
        Player currentPlayer = field.getCurrentPlayer();
        try {
            if (field.movePiece(rowFrom, colFrom, rowTo, colTo)) {
                System.out.println("Moved " + currentPlayer.toString() + "(" + currentPlayer.getSymbol() + ")"
                        + " piece from " + matcher.group(2) + " to " + matcher.group(5));
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

        try {
            if (field.removeRedPiece(row, col)) {
                System.out.println("Removed red piece from " + matcher.group(2));
            } else {
                System.out.println("Couldn't remove red piece at " + matcher.group(2));
            }
        } catch (PiecesException e) {
            System.out.println(e.toString());
        }
    }

    private void printInfoAboutGame() {
        System.out.println("                    ========= DIAMOND =========\n");
        System.out.println("The goal of this game(win condition) is to occupy 4 corners of any square.");
        System.out.println("Game has two phases: Placement phase and Movement phase");
        System.out.println("You can only place pieces on to the board during Placement phase.");
        System.out.println("After placing all 24 pieces(12 each for player), the game proceeds to Movement phase.");
        System.out.println("During Movement phase, you can either move your piece to new coordinates(if they're connected)\n" +
                "or you can choose to remove a neutral piece.");
        System.out.println("Neutral(red) pieces enter the game via captures on triangles.");
        System.out.println("Neutral pieces can be removed only if there are no adjacent player pieces near.");
        System.out.println("Draw happens if in 50 turns no piece has been captured or neutral piece removed " +
                "or current player cannot perform a move.\n");
        System.out.println("To end the game, write EXIT at any time.");

        pressEnterKeyToContinue();
    }

    private void pressEnterKeyToContinue() {
        System.out.println("Press Enter key to continue...");
        scanner.nextLine();
    }

    private void playAgain() {
        System.out.println("Would you like to play again? [Yes/No]");
        String answer = scanner.nextLine().toUpperCase();

        switch (answer) {
            case "YES":
                System.out.println("Resetting the field");
                this.field = new Field();
                break;
            case "NO":
                System.out.println("Exiting the game");
                System.exit(0);
                break;
            default:
                System.out.println("Exiting anyway..");
                System.exit(0);
                break;
        }
    }

}

