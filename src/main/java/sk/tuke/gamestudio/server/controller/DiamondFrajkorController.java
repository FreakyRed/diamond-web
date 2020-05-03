package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Scope;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.diamond.frajkor.webui.Command;
import sk.tuke.gamestudio.game.diamond.frajkor.webui.WebUI;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class DiamondFrajkorController {

    private final WebUI webUI;

    private final ScoreService scoreService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public DiamondFrajkorController(ScoreService scoreService, CommentService commentService, RatingService ratingService) {
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        this.webUI = new WebUI(scoreService,ratingService,commentService);
    }

    @RequestMapping("/diamond-frajkor")
    public String diamond(@RequestParam(value = "command", required = false) String command,
                          @RequestParam(value = "row", required = false) String rowFrom,
                          @RequestParam(value = "column", required = false) String columnFrom,
//                          @RequestParam(value = "rowTo", required = false) String rowTo,
//                          @RequestParam(value = "columnTo", required = false) String columnTo,
                          Model model) {

        Command cmd;

        if (command == null) {
            cmd = ((rowFrom != null) && (columnFrom != null)) ? Command.PLACE : Command.NEW;
        }else if(command.equals("new")){
            cmd = Command.NEW;
        }else if(webUI.isPlacementPhase()){
            cmd = Command.PLACE;
        }else{
            cmd = Command.MOVE;
        }

        webUI.processCommand(cmd, rowFrom, columnFrom);
        model.addAttribute("webUI",webUI);
        List<Score> bestScores = scoreService.getBestScores("diamond-frajkor");
        List<Comment> comments = commentService.getComments("diamond-frajkor");
        int ratings = ratingService.getAverageRating("diamond-frajkor");

        model.addAttribute("scores", bestScores);
        model.addAttribute("comments",comments);
        model.addAttribute("averageRating",ratings);

        return "diamond-frajkor";
    }


}
