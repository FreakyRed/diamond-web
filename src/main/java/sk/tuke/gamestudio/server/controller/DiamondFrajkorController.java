package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Scope;
import sk.tuke.gamestudio.game.diamond.frajkor.core.*;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.gamephase.WrongGamePhaseException;
import sk.tuke.gamestudio.game.diamond.frajkor.core.exceptions.pieces.PiecesException;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.EasyBot;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

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
    private EasyBot easyBot;

    @RequestMapping
    public String diamond(String row, String column, Model model) {
        if(this.field == null) {
            createNewGame();
            easyBot = new EasyBot(field);
        }

        try{
                if(field.getGamePhase() == GamePhase.PLACEMENT){
                    field.placePiece(Integer.parseInt(row),Integer.parseInt(column));
                    if(field.getCurrentPlayer() == Player.WHITE) easyBot.placePiece();
                }
                else{
                    field.movePiece(Integer.parseInt(row),Integer.parseInt(column),0,0);
                    if(field.getCurrentPlayer() == Player.WHITE) easyBot.movePiece();
                }
            }catch (NumberFormatException | PiecesException | WrongGamePhaseException | NullPointerException e){
                e.printStackTrace();
            }

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

    public GamePhase getGamePhase(){
        return field.getGamePhase();
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr class='field-row'>\n");
            for (int column = 0; column < field.getColumnCount(row); column++) {
                Piece piece = field.getPiece(row, column);
                sb.append("<td>\n");
                if(getGamePhase() == GamePhase.PLACEMENT) {
                    sb.append("<a href='" +
                            String.format("/diamond-frajkor?row=%s&column=%s", row, column)
                            + "' class='piece-link'>");
                    sb.append("<div class='circle piece-" + getClassName(piece) + "'></div>");
                    sb.append("</a>\n");
                }
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }


    private void createNewGame() {
        this.field = new Field();
        this.easyBot = new EasyBot(field);
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
        model.addAttribute("comments", commentService.getComments(GAME_NAME));
        model.addAttribute("gameField",field.getField());
    }

}
