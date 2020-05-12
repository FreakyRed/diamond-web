package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Scope;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.diamond.frajkor.core.*;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase.WrongGamePhaseException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.Bot;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.EasyBot;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/diamond-frajkor")
public class DiamondFrajkorController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private static final String GAME_NAME = "diamond-frajkor";
    private Field field;
    private Bot bot;

    @RequestMapping
    public String diamond(String row, String column, Model model) {
        if (this.field == null) {
            createNewGame();
            bot = new EasyBot(field);
        }

        if (getGameState() == GameState.SOLVED) {
            prepareModel(model);
            return "diamond-frajkor";
        }

        gameLoop(row, column);
        prepareModel(model);
        return "diamond-frajkor";
    }

    @RequestMapping("/rating")
    public String addRating(String rating, Model model) {
        ratingService.setRating(new Rating(System.getProperty("user.name"), GAME_NAME, Integer.parseInt(rating), new Date()));
        prepareModel(model);
        return "diamond-frajkor";
    }

    @RequestMapping("/comment")
    public String addComment(String comment, Model model) {
        commentService.addComment(new Comment(System.getProperty("user.name"), GAME_NAME, comment, new Date()));
        prepareModel(model);
        return "diamond-frajkor";
    }

    @RequestMapping("/new")
    public String newGame(Model model) {
        createNewGame();
        prepareModel(model);
        return "diamond-frajkor";
    }

    public GameState getGameState() {
        return field.getGameState();
    }

    public GamePhase getGamePhase() {
        return field.getGamePhase();
    }

    public Player getCurrentPlayer() {
        return field.getCurrentPlayer();
    }

    public boolean isGameDrawn() {
        return field.areDrawConditionsMet();
    }

    public boolean isGameWon() {
        return getGameState() == GameState.SOLVED && getCurrentPlayer() == Player.BLACK && !isGameDrawn();
    }

    public boolean isGameLost(){
        return getGameState() == GameState.SOLVED && getCurrentPlayer() == Player.WHITE;
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr class='field-row'>\n");
            for (int column = 0; column < field.getColumnCount(row); column++) {
                Piece piece = field.getPiece(row, column);
                sb.append("<td>\n");
                sb.append("<a href='" +
                        String.format("/diamond-frajkor?row=%s&column=%s", row, column)
                        + "' class='piece-link'>");
                sb.append("<div class='circle piece-" + getClassName(piece) + "'></div>");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }


    private void createNewGame() {
        this.field = new Field();
        this.bot = new EasyBot(field);
    }

    private String getClassName(Piece piece) {
        switch (piece.getPieceType()) {
            case EMPTY:
                return "empty";
            case BLACK:
                return "black";
            case WHITE:
                return "white";
            case NEUTRAL:
                return "neutral";
        }
        throw new IllegalArgumentException("State not supported" + piece.getPieceType());
    }

    private void prepareModel(Model model) {
        model.addAttribute("scores", scoreService.getBestScores(GAME_NAME));
        model.addAttribute("averageRating", ratingService.getAverageRating(GAME_NAME));
        model.addAttribute("playerRating", ratingService.getRating(GAME_NAME, System.getProperty("user.name")));
        model.addAttribute("comments", commentService.getComments(GAME_NAME).subList(0, 5));
        model.addAttribute("gameField", field.getField());
    }

    private Piece findFirstEmptyConnectedPiece(Piece piece) {
        return piece.getConnectedPieces().stream().filter(p -> p.getPieceType() == PieceType.EMPTY).findFirst().get();
    }

    private int findPieceInField(Piece piece, boolean isRow) {
        int row = 0, col = 0;
        for (int i = 0; i < field.getField().length; i++) {
            for (int j = 0; j < field.getField()[i].length; j++) {
                if (field.getField()[i][j].equals(piece)) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        return (isRow) ? row : col;
    }

    private void gameLoop(String row, String column) {
        if (row == null || column == null) {
            return;
        }

        try {
            int rowFrom = Integer.parseInt(row), colFrom = Integer.parseInt(column);

            if (field.getGamePhase() == GamePhase.PLACEMENT) {
                field.placePiece(rowFrom, colFrom);
            } else {
                if (field.getPiece(rowFrom, colFrom).getPieceType() == PieceType.NEUTRAL) {
                    field.removeRedPiece(rowFrom, colFrom);
                } else {
                    Piece piece = field.getPiece(rowFrom, colFrom);
                    Piece connectedPiece = findFirstEmptyConnectedPiece(piece);
                    field.movePiece(rowFrom, colFrom,
                            findPieceInField(connectedPiece, true), findPieceInField(connectedPiece, false));
                }
            }

                if (this.isGameWon()) {
                    scoreService.addScore(new Score(GAME_NAME, System.getProperty("user.name"), field.getScore(), new Date()));
                    return;
                }

                if (field.getCurrentPlayer() == Player.WHITE){
                    if ((getGamePhase() == GamePhase.PLACEMENT)) {
                        bot.placePiece();
                    } else {
                        bot.movePiece();
                    }
                }

                if(this.isGameLost()){
                    return;
                }

        } catch (NumberFormatException | PiecesException | WrongGamePhaseException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }


}
