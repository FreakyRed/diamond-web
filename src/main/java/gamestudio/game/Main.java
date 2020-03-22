package main.java.gamestudio.game;

import main.java.gamestudio.game.core.*;
import main.java.gamestudio.game.userinterface.ConsoleUI;
import main.java.gamestudio.game.core.Field;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        ConsoleUI consoleUI = new ConsoleUI(field);
        consoleUI.run();
    }
}
