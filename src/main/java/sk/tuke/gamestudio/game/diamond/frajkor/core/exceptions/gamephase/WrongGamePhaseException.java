package sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase;

public class WrongGamePhaseException extends Throwable {
    @Override
    public String toString() {
        return "Game is in the wrong game phase for you to do that";
    }
}
