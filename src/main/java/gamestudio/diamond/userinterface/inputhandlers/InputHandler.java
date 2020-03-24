package gamestudio.diamond.userinterface.inputhandlers;

import gamestudio.diamond.core.Field;
import gamestudio.diamond.core.Piece;

import java.util.List;

public abstract class InputHandler {
    private Field field;

    InputHandler(Field field){
        this.field = field;
    }

    abstract public void handleInput();
    abstract public boolean isDrawnByPlayer();
    abstract public void findPositionOfPiecesInField(List<Piece> pieces, StringBuilder stringBuilder);

    Field getField(){
        return this.field;
    }

}
