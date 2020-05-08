package sk.tuke.gamestudio.game.diamond.frajkor.userinterface;

import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;

import java.util.Random;

public abstract class Bot {
    Random random = new Random();
    Field field;

    Bot(Field field){
        this.field = field;
    }

    public abstract void placePiece();
    public abstract void movePiece();
}
