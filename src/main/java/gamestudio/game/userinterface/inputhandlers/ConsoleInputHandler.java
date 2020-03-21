package main.java.gamestudio.game.userinterface.inputhandlers;

import main.java.gamestudio.game.core.Field;
import main.java.gamestudio.game.core.GamePhase;
import main.java.gamestudio.game.core.Piece;
import main.java.gamestudio.game.exceptions.gamephase.WrongGamePhaseException;
import main.java.gamestudio.game.exceptions.pieces.PiecesException;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleInputHandler implements InputHandler {

    private Field field;
    private Scanner scanner = new Scanner(System.in);
    private boolean drawnByPlayer = false;
    private final Pattern INPUT_PATTERN_PLACEMENT = Pattern.compile("\\s*([A-U])([1-9])\\s*");
    private final Pattern INPUT_PATTERN_MOVEMENT = Pattern.compile("\\s*(M)(([A-U])([1-9]))(([A-U])([1-9]))\\s*");
    private final Pattern INPUT_PATTERN_REMOVE = Pattern.compile("\\s*(R)(([A-U])([1-9]))\\s*");
    private final Pattern INPUT_PATTERN_SHOW_CONNECTED = Pattern.compile("\\s*(SHOW)(([A-U])([1-9]))\\s*");

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
        String line = scanner.nextLine().strip().toUpperCase();

        if (line.equals("EXIT")) {
            System.exit(0);
        } else if (line.equals("DRAW")) {
            if (proposeDraw()) {
                return;
            }
            handleInputForPlacementPhase();
        }

        Matcher matcherPlace = INPUT_PATTERN_PLACEMENT.matcher(line);
        Matcher matcherShow = INPUT_PATTERN_SHOW_CONNECTED.matcher(line);

        if (matcherShow.matches()) {
            showConnectedPieces(matcherShow);
        } else if (matcherPlace.matches()) {
            placePieceToPosition(matcherPlace);
        } else {
            System.out.println("Bad input, please try again");
        }
    }

    private void showConnectedPieces(Matcher matcher) {
        int row = matcher.group(3).charAt(0) - 65;
        int col = Integer.parseInt(matcher.group(4)) - 1;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            List<Piece> connectedPieces = field.getConnectedPieces(row, col);
            field.findPositionInField(connectedPieces, stringBuilder);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Piece coordinates are not within the field");
            return;
        }

        for(int i = 2; i < stringBuilder.length(); i += 3){
            stringBuilder.insert(i," ");
        }

        System.out.println("Pieces connected to " + matcher.group(2) + ": " + stringBuilder.toString());
    }

    private void placePieceToPosition(Matcher matcher) {
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
        String line = scanner.nextLine().strip().toUpperCase();

        if (line.equals("EXIT")) {
            System.exit(0);
        } else if (line.equals("DRAW")) {
            if (proposeDraw()) {
                return;
            }
            handleInputForMovementPhase();
        }

        Matcher moveMatcher = INPUT_PATTERN_MOVEMENT.matcher(line);
        Matcher removeMatcher = INPUT_PATTERN_REMOVE.matcher(line);
        Matcher showMatcher = INPUT_PATTERN_SHOW_CONNECTED.matcher(line);

        if(showMatcher.matches()) {
            showConnectedPieces(showMatcher);
        }
        else if (moveMatcher.matches()) {
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
        String currentPlayer = field.getCurrentPlayer().toString() + "(" + getSymbolForPlayer() + ")";
        try {
            if (field.movePiece(rowFrom, colFrom, rowTo, colTo)) {
                System.out.println("Moved " + currentPlayer
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

    private boolean proposeDraw() {
        System.out.println("Player proposed a draw. Do you wish to draw the game?[yes/no]");
        String answer = scanner.nextLine().toUpperCase();

        if (answer.equals("YES")) {
            this.drawnByPlayer = true;
            return true;
        } else if (answer.equals("NO")) {
            System.out.println("You have refused to draw the game. Game continues.");
            return false;
        }
        System.out.println("Wrong choice.");
        return false;
    }

    public boolean isDrawnByPlayer() {
        return drawnByPlayer;
    }
}
