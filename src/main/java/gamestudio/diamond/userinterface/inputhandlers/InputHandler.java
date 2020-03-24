package gamestudio.diamond.userinterface.inputhandlers;

import gamestudio.diamond.core.Field;
import gamestudio.diamond.core.Piece;

import java.util.List;

public abstract class InputHandler {
    private Field field;
    private boolean drawnByPlayer = false;

    InputHandler(Field field){
        this.field = field;
    }

    abstract public void handleInput();
    abstract void proposeDraw();
    abstract public void findPositionOfPiecesInField(List<Piece> pieces, StringBuilder stringBuilder);

    Field getField(){
        return this.field;
    }

    void setDrawnByPlayer(boolean drawn){
        drawnByPlayer = drawn;
    }

    public boolean isDrawnByPlayer(){
        return drawnByPlayer;
    }

}
