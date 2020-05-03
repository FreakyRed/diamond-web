package sk.tuke.gamestudio.game.diamond.frajkor.webui;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;
import sk.tuke.gamestudio.game.diamond.frajkor.core.GameState;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Piece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase.WrongGamePhaseException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.Date;

public class WebUI {
    private final String GAME_NAME = "diamond-frajkor";
    private Field field;
    private boolean gameEnded = false;
    private int finalScore = 0;

    private ScoreService scoreService;

    public WebUI(ScoreService scoreService) {
        this.scoreService = scoreService;
        createField();
    }

    private void createField() {
        this.gameEnded = false;
        this.finalScore = 0;
        this.field = new Field();
    }

    public void processCommand(Command command, String rowString, String columnString) {
        if (command == Command.NEW) {
            createField();
            return;
        }

        if (gameEnded) {
            return;
        }

        int row = Integer.parseInt(rowString);
        int column = Integer.parseInt(columnString);

//        switch (command) {
//            case PLACE:
//                try {
//                    field.placePiece(row, column);
//                } catch (WrongGamePhaseException e) {
//                    return;
//                }
//                break;
//            case MOVE:
//                try {
//                    field.movePiece(row, column, 0, 0);
//                } catch (PiecesException | WrongGamePhaseException e) {
//                    return;
//                }
//                break;
//            case DRAW:
//                break;
//        }

        try {
            field.placePiece(row, column);
        } catch (WrongGamePhaseException e) {
            return;
        }

        if (isGameEnded()) {
            gameEnded = true;
            if (isGameWon()) {
                finalScore = field.getScore();
                scoreService.addScore(new Score(GAME_NAME, System.getProperty("user.name"), finalScore, new Date()));
            }
        }

    }

    public Field getField() {
        return field;
    }

    public boolean isGameWon() {
        return field.getGameState() == GameState.SOLVED;
    }

    public boolean isGameEnded() {
        return isGameWon();
    }

    public int getFinalScore() {
        return finalScore;
    }

    public ViewPiece getViewPiece(int row, int column) {
        return new ViewPiece(field.getPiece(row, column), row, column);
    }

}
