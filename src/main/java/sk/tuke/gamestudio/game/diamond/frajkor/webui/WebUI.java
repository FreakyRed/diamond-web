package sk.tuke.gamestudio.game.diamond.frajkor.webui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Field;
import sk.tuke.gamestudio.game.diamond.frajkor.core.GamePhase;
import sk.tuke.gamestudio.game.diamond.frajkor.core.GameState;
import sk.tuke.gamestudio.game.diamond.frajkor.core.Piece;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase.WrongGamePhaseException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.Date;

public class WebUI {
    private final String GAME_NAME = "diamond-frajkor";
    private Field field;
    private boolean gameEnded = false;
    private int finalScore = 0;

    private ScoreService scoreService;
    private RatingService ratingService;
    private CommentService commentService;

    public WebUI(ScoreService scoreService, RatingService ratingService, CommentService commentService) {
        this.scoreService = scoreService;
        this.ratingService = ratingService;
        this.commentService = commentService;
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

        switch (command) {
            case PLACE:
                try {
                    field.placePiece(row, column);
                } catch (WrongGamePhaseException e) {
                    return;
                }
                break;
            case MOVE:
                try {
                    field.movePiece(row, column, 0, 0);
                } catch (PiecesException | WrongGamePhaseException e) {
                    return;
                }
                break;
            case DRAW:
                break;
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

    public String getGameState(){
        return field.getGameState().toString().toUpperCase();
    }

    public ViewPiece getViewPiece(int row, int column) {
        return new ViewPiece(field.getPiece(row, column), row, column);
    }

    public String getGamePhase() {
        return field.getGamePhase().toString().toUpperCase();
    }

    public boolean isPlacementPhase(){
        return field.getGamePhase() == GamePhase.PLACEMENT;
    }

}
