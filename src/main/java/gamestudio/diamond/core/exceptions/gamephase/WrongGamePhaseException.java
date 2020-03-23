package gamestudio.diamond.core.exceptions.gamephase;

public class WrongGamePhaseException extends Throwable {
    @Override
    public String toString() {
        return "Game is in the wrong game phase for you to do that";
    }
}
