package com.company;

import com.company.gamestudio.game.core.*;
import com.company.gamestudio.game.userinterface.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        ConsoleUI consoleUI = new ConsoleUI(field);
        consoleUI.run();
    }
}
