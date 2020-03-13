package com.company.gamestudio.game.userinterface;

import com.company.gamestudio.game.core.Field;
import com.company.gamestudio.game.core.GamePhase;
import com.company.gamestudio.game.core.GameState;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI implements UI {
    private Field field;
    private Scanner scanner = new Scanner(System.in);

    final Pattern INPUT_PATTERN_PLACEMENT = Pattern.compile("([A-U])([1-6])");
    final Pattern INPUT_PATTERN_MOVEMENT = Pattern.compile("(([A-U])([1-6]))(([A-U])([1-6]))");

    public ConsoleUI(Field field) {
        this.field = field;
    }

    @Override
    public void run() {
       do {
           printField();
           processInput();
       }
       while (field.getGameState() == GameState.PLAYING);
       printField();
       printFinalMessage();
    }

    private void printFinalMessage(){
        if(field.getGameState() == GameState.SOLVED){
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
            processInputPlacement();
        } else {
            processInputMovement();
        }
    }

    private void processInputPlacement() {
        System.out.println("Enter coordinates for placement of your piece (e.g. A1, K3):");
        String line = scanner.nextLine().toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }

        Matcher m = INPUT_PATTERN_PLACEMENT.matcher(line);
        if (m.matches()) {
            int row = m.group(1).charAt(0) - 65;
            int col = Integer.parseInt(m.group(2)) - 1;

            if (row < 0 || row >= field.getField().length || col < 0 || col >= field.getField()[row].length){
                System.out.println("Entered coordinates are not within the field");
                return;
            }

            if (field.placePiece(row, col)) {
                System.out.println("Placed " + field.getCurrentPlayer().toString() + " piece to " + m.group(1) + m.group(2));
            } else {
                System.out.println("Unable to place your piece (piece is already there)");
            }
        }
    }

    private void processInputMovement() {
        System.out.println("Enter a movement command (e.g. A1B2, U1T2):");

        String line = scanner.nextLine().toUpperCase();
        if (line.equals("EXIT")) {
            System.exit(0);
        }

        Matcher m = INPUT_PATTERN_MOVEMENT.matcher(line);
        if (m.matches()) {
            int row = m.group(2).charAt(0) - 65;
            int col = Integer.parseInt(m.group(3)) - 1;
            System.out.println();
        } else {
            System.out.println("Bad input, try again");
        }
    }
}
