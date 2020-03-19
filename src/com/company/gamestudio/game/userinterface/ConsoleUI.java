package com.company.gamestudio.game.userinterface;

import com.company.gamestudio.game.core.Field;
import com.company.gamestudio.game.core.GameState;
import com.company.gamestudio.game.core.Piece;
import com.company.gamestudio.game.userinterface.inputhandlers.ConsoleInputHandler;
import com.company.gamestudio.game.userinterface.inputhandlers.InputHandler;

import java.util.Scanner;

public class ConsoleUI implements UI {
    private Field field;
    private InputHandler inputHandler;

    public ConsoleUI(Field field) {
        this.field = field;
        this.inputHandler = new ConsoleInputHandler(field);
    }

    @Override
    public void run() {
        printInfoAboutGame();
        do {
            printField();
            printSquareCoordinates();
            handleInput();
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

    private void printGameDetails() {
        System.out.print("\u001B[34mGamephase: \u001B[0m" + field.getGamePhase() +
                "          " + "\u001B[34mCurrent Player: \u001B[0m" + field.getCurrentPlayer().toString());
        System.out.print("(" + getSymbolForPlayer() + ")\n");
    }

    private void printField() {
        printGameDetails();
        printFieldBody();
    }

    private void printFieldBody(){
        System.out.println("\u001B[33m==========================================================\u001B[0m");
        printUpperPart();
        printMiddlePart(9);
        printLowerPart();
        System.out.println("\u001B[33m==========================================================\u001B[0m");
    }

    private void printUpperPart() {
        printFirstPart(0, 'A');
        printSecondPart(3, 'D');
        printThirdPart(6, 'G');
    }

    private void printLowerPart() {
        printThirdPart(12, 'M');
        printSecondPart(15, 'P');
        printFirstPart(18, 'S');
    }

    private void printFirstPart(int i, char symbol) {
        System.out.print("\u001B[33m");
        System.out.println(symbol + "                             " + getCharacter(field.getField()[i][0]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 1) + "                         " + getCharacter(field.getField()[i + 1][0]) +
                "       " + getCharacter(field.getField()[i + 1][1]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 2) + "                             " + getCharacter(field.getField()[i + 2][0]));
    }

    private void printSecondPart(int i, char symbol) {
        System.out.print("\u001B[33m");
        System.out.println(symbol + "                     " + getCharacter(field.getField()[i][0]) +
                "               " + getCharacter(field.getField()[i][1]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 1) + "                 " + getCharacter(field.getField()[i + 1][0]) +
                "       " + getCharacter(field.getField()[i + 1][1]) + "       " + getCharacter(field.getField()[i + 1][2]) +
                "       " + getCharacter(field.getField()[i + 1][3]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 2) + "                     " + getCharacter(field.getField()[i + 2][0]) +
                "               " + getCharacter(field.getField()[i + 2][1]));
    }

    private void printThirdPart(int i, char symbol) {
        System.out.print("\u001B[33m");
        System.out.println(symbol + "              " + getCharacter(field.getField()[i][0]) + "              " +
                getCharacter(field.getField()[i][1]) + "               " +
                getCharacter(field.getField()[i][2]) + "             ");
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 1) + "          " + getCharacter(field.getField()[i + 1][0]) +
                "       " + getCharacter(field.getField()[i + 1][1]) + "     " + getCharacter(field.getField()[i + 1][2]) +
                "         " + getCharacter(field.getField()[i + 1][3]) + "     " + getCharacter(field.getField()[i + 1][4]) +
                "        " + getCharacter(field.getField()[i + 1][5]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 2) + "              " + getCharacter(field.getField()[i + 2][0]) +
                "              " + getCharacter(field.getField()[i + 2][1]) +
                "               " + getCharacter(field.getField()[i + 2][2]));
    }

    private void printMiddlePart(int i) {
        System.out.print("\u001B[33m");
        System.out.println("J       " + getCharacter(field.getField()[i][0]) + "              " + getCharacter(field.getField()[i][1]) +
                "               " + getCharacter(field.getField()[i][2]) + "               " + getCharacter(field.getField()[i][3]));
        System.out.print("\u001B[33m");
        System.out.println("K          " + getCharacter(field.getField()[i+1][0]) + "      " + getCharacter(field.getField()[i+1][1]) +
                "       " + getCharacter(field.getField()[i+1][2]) + "       " + getCharacter(field.getField()[i+1][3]) +
                "        " + getCharacter(field.getField()[i+1][4]) + "       " + getCharacter(field.getField()[i+1][5]));
        System.out.print("\u001B[33m");
        System.out.println("L       " + getCharacter(field.getField()[i+2][0]) + "              " + getCharacter(field.getField()[i+2][1]) +
                "               " + getCharacter(field.getField()[i +2][2]) + "               " + getCharacter(field.getField()[i +2][3]));
    }

    private String getCharacter(Piece piece) {
        switch (piece.getPieceType()) {
            case EMPTY:
                return ("\u001B[34m" + "." + "\u001B[0m");
            case NEUTRAL:
                return ("\u001B[31m" + "#" + "\u001B[0m");
            case BLACK:
                return ("\u001B[37m" + "&" + "\u001B[0m");
            case WHITE:
                return ("\u001B[30m" + "$" + "\u001B[0m");
            default:
                throw new IllegalStateException();
        }
    }

    private String getSymbolForPlayer(){
        switch (field.getCurrentPlayer()){
            case BLACK:
                return ("\u001B[37m" + "&" + "\u001B[0m");
            case WHITE:
                return ("\u001B[30m" + "$" + "\u001B[0m");
            default: throw new IllegalStateException();
        }
    }

    private void handleInput() {
        inputHandler.handleInput();
    }

    private void printInfoAboutGame() {
        System.out.println("\n\u001B[33m         ========= DIAMOND =========\u001B[0m");
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
        new Scanner(System.in).nextLine();
    }

    private void playAgain() {
        System.out.println("\u001B[35mWould you like to play again? [Yes/No]\u001B[0m");
        String answer = new Scanner(System.in).nextLine().toUpperCase();

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

    private void printSquareCoordinates() {
        System.out.print("\u001B[34mSquares: \u001B[0m");
        for (int i = 0; i < field.getSquareCoordinates().length; i++) {
            System.out.print(field.getSquareCoordinates()[i] + " ");
            if (i == field.getSquareCoordinates().length/2 - 2) {
                System.out.println();
            }
        }
        System.out.println();
    }
}

