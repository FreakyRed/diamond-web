package gamestudio.diamond;

import gamestudio.diamond.core.Field;
import gamestudio.diamond.userinterface.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        ConsoleUI consoleUI = new ConsoleUI(field);
        consoleUI.run();
    }
}
